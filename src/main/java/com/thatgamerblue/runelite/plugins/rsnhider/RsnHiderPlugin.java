/*
 * Copyright (c) 2020, ThatGamerBlue <thatgamerblue@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.thatgamerblue.runelite.plugins.rsnhider;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.ScriptID;
import net.runelite.api.events.BeforeRender;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.OverheadTextChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;
import java.util.Random;

/*
Mental breakdown 2: electric boogaloo

Alexa, play sea shanty two.
Peace to:
	r189, he.cc
*/
@PluginDescriptor(
	name = "RSN Hider",
	description = "Hides your rsn for streamers.",
	tags = {"twitch"},
	enabledByDefault = false
)
public class RsnHiderPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private RsnHiderConfig config;

	private static String fakeRsn;

	private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	@Provides
	private RsnHiderConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(RsnHiderConfig.class);
	}

public static String[] NAMES = {"immygpimp", "pistcuettyple", "edi_wator", "Doe158", "SGHAV3", "The_hellooser", "Roup", "AVasTyler", "chetrivris", "Facejector5", "InternetYusNoadforne2", "DJBattyster99", "LotIsnier", "vomazeutta", "adaxtare7", "anchess_rx", "Handen-id", "skepthsr", "latecocks", "The_Grutic", "TheMightXates", "Kix0Paphonia", "ElwettickFeng", "stickin23", "yousismchair", "thefunkalk1", "batgopper", "tereoordones", "Glashfar", "shadow_throid", "isalomankativechampi", "k4rm", "Shiggyantza", "PM_MROSESDSBAT", "Snuwing", "Rykal431", "Mumashwit", "The_arayophelphill", "justthecommandagoll", "theclamood", "ittovan_mithin", "Psrof3s", "Forden_Lost_Flow-Iy_", "kblragg", "inico1221", "starson_a_monalah", "Eles_phence_ube", "YouYoutPossCifat", "loghbasdobagant", "ThisDillingWhite", "JamicakMefass", "Madcheewingcatua", "HombaloWengOnYous", "Bearsh", "TakZ35", "Bacendaul", "Low_Vits", "psproocoldbecozish", "Lordjemfer", "Aqimlobhdice", "sam23217", "ChaozaOwn", "hamballtrombsed", "cimertruster", "masis_sprofcont", "Moonme_SunDamisti", "ktimjulskips", "streverdefist", "Dernout", "its_staffen899", "betanderthd", "fm8717", "i_rever_", "KellowPosty", "Kindopee747", "lightbead", "SkYMG", "haverockel8908", "casu0st", "explesical", "Wilin1140", "Xefg1980", "the_grife_exetted", "Statt01357", "Fldsangurnt_count6", "Shammer1956", "DBa_SeAD", "baninassimmydel", "RednaxX", "BettyConate", "PoldoSnifeWuppies", "fufkinsaffe", "rowstchesselfpucksonx", "epcoist1987", "xeayepplyX", "dallmillian", "K-Y_Crizard", "The_Buds", "JimRZainCharopo", "Encertrum", "Colfuck_offace", "Bnarliguss", "Hot_Have_Morn", "ineryburgayoak", "mr_man_peetere", "goltheyess", "LaumseYou", "themcfortry42", "jcm66", "BrefSnapfulet", "ducksteder1", "HereymcDet", "RuiatioAm51", "HandlerkDruntum", "FodeYN", "BanHassHoads", "baruanc216", "beto_b990", "o", "lossjam", "JaqEG_racal7", "myLoveDoRin", "Cotch", "mastenstabLAMON", "Paxi_Kids69", "ChrisInAnglyNeel", "ol_i-van", "Whonezelf02", "Panda_Cool99", "Qingersgrees", "MadganThatharth", "jedkelxon", "isemper2", "Daralwalf", "InsanchyRank", "Britzesh", "HoVescrineerteAsexis", "jomeisdeadbaron", "IgusticUtman", "tlairedark1999", "DepmeticBragon", "galotickus", "Bot-Sevenkaticward", "migi1666", "LungNacks151", "complehonshinasu", "yhactio", "raovalu", "TromeFighty", "GarofBlugDeTacro", "Zeef_LK", "jgshour", "ImYountChris25", "Laydiv", "DrawnOloonPecks", "sziebox", "Tg2Bashaouz", "holegood430", "Samurrious", "RSBLWLC", "brolegolla", "magicam", "BVS56", "Deezzygoot77", "arganja", "Bigbugy", "strage08", "dyok1238", "itsymmath", "secfornet", "Gharninlenn", "butthabium", "dwebde", "fto", "sysypster", "Karelle", "hiebooman80", "Dimarajesis", "Aghtnity", "scc9825", "Naixa", "ilifffishkilt", "ChuddhesMD7", "FlC42309", "maxisnagitethepash", "cs_yuusha", "bwystropheloman", "seebie61", "TheBibBumbie", "therecoberdompran", "TobaBucklinx", "nulkerb77", "acd96", "neogerbar", "Nozer_Me_Here", "cheekybree", "ds_chinisfous", "theturtat62", "thejalfell", "nishpirg", "Speptheone183", "ecitz", "FizartDi1g", "cennylomeistis", "axnix1", "Karajo", "DJOCOPE_DAV00", "ytzt", "danner157sx", "axel_courtrum", "t95-", "thorsago", "flrrnrn_e", "ceterman6000", "Ohnier25", "thunduger", "PothamJah", "Yyriobuivesto", "burrettheaeliath", "Bualoh_mira_its_hucker-1fStool", "lts_mungle", "a-trapliktal", "matkarwh3484", "zapenjest", "YeonZumpers", "Aploop89", "Othrell", "Andre-X", "1suptreid", "phrosmflap", "Enamansafilotivm", "allun", "willsceched", "dunixa1099", "Deskra", "sammt3", "myMakeJiahso", "someishspinfirafus", "Creatingtamana123", "St26540", "ConnamoneMidey", "Ginal__it-pean", "TheMasterlopsticks", "MostLoller86", "Shanken_Pirate", "Ainwerd", "bausernakot14", "Goopydetthind", "dead4skills4", "screthictreopyy", "VinNahCoch", "Gilltuter031", "lord_live_captain", "SucSunnator", "PaliceKulipa9", "Ooper_McBesty", "Asker27", "gicked651", "realhyquintro", "better_fumples", "the_bennah", "teblabutter2", "twrmdz05", "ILiFK", "kyonald_eer", "thenughmilsewarp", "ComlemNobber4Soxxx", "TheGlober", "dt0kens123", "Solyistini", "chew423", "WellSpunVet", "taulati", "sexionanina", "Neo30183", "Bullmoranabla", "dankmaft", "LarthCow", "Mike3337", "beefjuady", "HumooonB1db", "pkin017", "capty_crick-iong", "Carlad0t", "taracid", "6hidaosidiappussopassian", "hive66662", "lorecrazy", "AvenHexAt", "Robosevian90", "kenbeini", "aamyamproged", "MrStefermo", "c0br32p305", "Hell_McBlack9012", "pigercky55", "DighSavurug", "dresroy", "TurblyBnee", "amanja13", "Samgision", "FOLS2", "dombbr1171", "challeedatpavagul", "KingerThass", "SAustaderman", "Ad_emo", "KikaZayray", "Apararom", "Chocotions", "Lone_Altai", "Ctoolstrice002", "Essaualmybear", "airinjabot", "WhitAXlightlyPolk", "ALI_7_X0x", "P-DEUR", "boohorm20f", "ujacanjam", "1il2550", "Asshin_cake_", "darfle_21", "yourmanessionbarrer", "guicommerc", "Herralm82", "southkid94", "BlunchJuscas", "BigWhatUsAn", "Acesoftacat", "butswins", "DrTollemer", "TheCheesitie", "Lanedonpai", "The_Blunch_Lepti", "auturtassluts", "NagineDaggo", "boodly23", "sweatdead", "Gerolon", "darkhan25", "kenovylabo281", "Themony885", "SirLessureCurtMes", "Swankzerdants", "FillofatzCat-Wit", "xBook", "Woraltuctorou", "gemaho2", "Runnmansten", "MilkRbo", "EprainsK", "LisholeZamilla", "lyve177", "Davil-Teppons", "gettensubread", "SupaiousWith", "woontallow", "riborttim", "chipa_gouse", "XcactleznD", "NezoroG", "raybengurr", "QuandyRightman046", "chalkhatt7", "PulteRife", "NeburNotox", "CompleOmoranican", "Komospercy", "Kridzzailut", "Phorolator69", "jjewty1989", "DuderFTYR", "cammah", "booblemanicarman01", "Hatthenpird4", "smart_bisco", "Elgelaac", "Basblappi", "OnePixkot", "useln11817", "IsaChinyTurn", "ssypic82", "caibac25611", "phackvaBlacka", "aprainbos", "godstaceman53", "ebbermschow", "mrsmic", "GreyMcThehress", "omiliin12", "crairro76", "SoulssBroicer", "DarthanningSwelvyts", "Diggypho", "therinajables", "whatspaceginyone", "gotc0", "avis011201017", "XX-WIL-I-80", "scortyfich", "purati", "Diands", "NotStarmy58", "WeverCatas", "anyemdfwilling", "LurtyMaker1", "toGrancat", "FarthServer", "xXLex_Pangus", "Nourforn", "danceris_thom", "s2spe264", "miliffifichella", "upnothesz", "yuconncutts", "sweetbag", "gaeayrack", "palpal16", "jahgar426", "TheLumpantic_I", "seapthegool", "the_leatells", "Nbit9z", "MOkazoo", "RungleRoid", "126K", "HawlooSwacemx", "superzot", "jokerbooky", "JAmashian", "Liluve_Varku", "Ugom0nManix", "Jimmytords", "windesterzer3963", "Bigfrophelogicloo", "itspeaching", "subice868", "c0fkbtaur", "ExitHealnisebun", "MarkaGox", "PlainDusck", "Fleesaspersacragger", "FlameEateCumpuser", "LoodMubsdrack", "AserTro0se", "ETW", "rumpyunkon", "TheDogingistus", "gualasausu", "HoteGuyaccm", "cammer86", "ReallyAlocx", "cjducsc", "GoNuta", "stiddard11", "leckraysc", "nt4244", "EsexDuckzoshilicho", "Ohuai", "KaythaShenius", "fain_and_wade", "sinnyboooro", "tigfucked", "SlattyMcBees", "Bulone", "TheComegain92", "Meantherbird", "erasa_Alligal", "ii_het_poft", "TheRarbosag", "Detoralian", "Averman", "the_co_a_picmacate", "i_Buffed_HarleckFafSanPoJ", "DY_Pepper", "Bubaloller", "jabootdoy", "Milding_Lady_Br", "todricfu", "Mexygal", "Nickle77", "ruicklight", "P-ME_MOUAY_", "Naga_d300", "90k550", "digger91", "SpedingPouldJoir", "gwips32", "UTs", "doctormermalic", "_Roozle_Ruggu", "colonig", "magureafasea", "TheRabyHowinDany", "Sk00pitule", "SmysticEboant", "kerlz", "kyfversd0908", "rusty03010", "AProSnowR42", "jdm112", "Slazyhumo", "SwAmokutka", "fasticabeazlapack", "babboyoik", "IssantmyCoves", "Fdnempsolegaust", "chatnaffel", "NianAndoic", "Kbonaton_kude", "ElpelmistCowe121", "zonythements", "ToodooPoker", "alkraver", "Check2_Ouras", "Terriancakes", "gripfalment", "NooneGuitivithethe-Fo", "Bassedsheep", "KurnShimmery_", "Spappyster", "Khopios", "MySexio", "gowelout_it", "doltehot1120", "edoman", "jottmes", "jero_1101", "wutjudero", "seccoff", "mjtnqu0ts", "JacksMcLuckle", "progicman", "williebin979", "uchicact98", "hellast26", "LoveOverDogo", "cs68600", "oticwickbii", "sadget-eob", "granginglam", "Zonathast25", "DupFubus419", "minkerydome", "wessafuy", "Obalac", "Avanthroast", "yauratear27", "puncher_condred5_", "Disaganco3", "WhofGooble", "MeasterChicken", "IblamedosCoolia", "Phhizza_Gop", "MacBeller", "danknokmd", "Ma2wolfgood", "RogalDadcorris", "JusttheCommatogirate", "CraDnamano", "peikak", "Zownnijnakai", "ippperiganator", "oldrision", "cat_useannmass", "warpustian", "Bluestick_608", "outafinthemy", "Whone_stackgorer", "Yunderthedamsone", "cocyslayer", "habars19", "TheALakersPeeche", "Quercoje1060", "riggeric", "bigarilaw", "DKMister", "ipelogsorch", "moggysseak", "Marcinal_Actor", "Bignoat_house", "funkycle", "Tigmarper", "dudespagmunn", "bananapron", "Shewurper", "BURBigYox", "Mo-rik_es_ap", "Fankaloo", "Vinzumshat", "nookays", "OneEtEatPorty", "Skyzzrjb", "RIoRizzlyPatama", "jastuch", "Cibanomaker", "noidjohntn", "Ethinothenamethen98", "mumbusidertolent", "Un_evthen", "kunker1994", "SinX82", "ZiFrovePynio", "Zamster20", "maldam6895", "novary802", "imtran98", "RustCanders201", "vqapshark112", "Tra___Socious_Bout", "beverkeskilly", "RakTheDeche", "oshit", "powertiantbee", "rl3k3c", "GenerS9", "moneybb", "Narpak", "miderburn", "Flustius11", "Rucknowpoming", "steelrunsti11986", "Inoguy-Silon", "Fisesair", "goder1kbea", "yailrouse", "ousthew1", "Gayinfellof", "oshalwynderbro", "MACools_offidell", "Dundeh25", "seCQeeverRanSem", "NakingCantrey", "IssItCC", "DBSFbrosco", "BrEXNo_TOIN_2-WEH_MANPICK", "iwfellander", "_Dunizz2", "icnixuar00015", "VACHEHCPAT", "addocadrax99", "the_beard_1075", "Westaf-Hanta_Clid", "SyVemaster", "MarkinSentiey", "landombut", "Cluchinomi67", "toablotoy33", "Zyarthler", "MrSonal", "rihatchus", "capertal", "LansterAnosig", "sampletooz", "Warm10Nom", "Vickezz", "tremph01", "eled_kin_tsa", "UFz_", "PooldtionDirala", "NohratoK", "Garmons", "ForPuckets", "onemanfruppin", "Vaov", "manonecrato", "layleywaee", "Done-you_Chill", "mjk003", "PennyMynattait", "Bubwender_benny", "profacamillou", "EvinDF", "Skinkklinds", "Misterslascie", "ATAEDx", "JohnnyJulie74", "Vorerzo", "mimmywardcho", "SiranSticken", "-gooding_peylig", "Ash_taus", "Guynessmitt", "Sbrin_rule", "3JohnnyHerfs", "jast151", "gaast_ts3", "mikesqatley", "mermon17", "bobbyricks7423", "vaseryra409", "Thispuprighted", "wigzoffie", "noohiepheemers", "Head_Master031", "HungeJusterWithTurgio", "cattainhcg", "Zampinstie", "ChammanTh", "AdleDerris", "SupgraTaBiech", "loopbonnerk", "iliveclapboit", "skankit", "hambongk", "SleekLulle1", "ilyoge", "LicidMous", "PooThing65", "Seunding_Neblay", "muntalystaulwisha", "yonginderglurd3207", "jrpockins", "sampleboy10", "pui837", "lownNick19701", "illocketc", "bigtwideo", "TGMDMooker", "Yearalgass400", "codes_cheaser", "BattandsofCheis", "Beatlock65", "ghemrownomatrabby", "LordofAnStons", "jcaur0s", "Squirrelboys", "nakri", "Haltheshawn", "cogote_manc", "Freb_im_Arginy", "xYottan", "Rangungle2", "boneroregd", "pressvorle", "the_boolen", "Imnice_of_thinker", "silverbie", "Rualmuffon", "FPOND906", "TekerTV", "SheatMech", "Egg20s", "nukoosea", "bdbullate60", "VighingNoCommerto", "KnutatorFucks", "GringinWackingMasane", "todda", "Quadthewrite", "fugitalreef", "slicegamehurdorg", "plornboy", "Brank34", "CleenDiac0", "CuPigViddiggSus", "GASterNusty", "Primnwl", "Yame_Chrisactio", "DakarsDGCdester215", "kot_wulkerbush", "Maihook", "BJA1995", "Mysponker1", "joeinfust", "____Hoode_of_Bick_your_", "MrSlutz", "morro99", "Stecept", "AriHamFG", "iVinden74", "DiamBravel", "American373", "GhillViller", "xatan_quidman", "MickElp01", "AathualSwarbs", "YnawajaB", "froetdeepers3", "Moohbeespakerpen", "axise226", "kellientyford", "eliquiniped", "mfodrade", "VillurinaWino", "ZembDrank", "barthdog", "InheremChangous", "pabydourfrom", "meillingn", "memejayglas", "Ven-Grats", "apitEfcromoniz", "WhotRodYingeFone", "SALW", "generspickjokelphap", "ashenti_225", "Impornis420dy", "rolschanson2", "jack_as_jayye", "Rasion_Fl", "Lucspurf", "DuussuSDreadhung", "Silennatson", "hiffbiragydurd", "Nicktystrion", "Squidd3", "paburtlebone", "De-Langus-10", "xylent_J-", "honyflyanturgers", "leartanth", "Qiotently_ul", "80yusobraizer", "MadaB-Thand", "folshesymir", "Chimaih", "Riagor77", "jdeaboss", "203lz8408_2", "th3sk0f", "DonoFoussTeonI1", "Ljtcam", "sickonethundy", "High-e_Luda", "bigsleard23", "ReadermatteGuitars", "KaraFoToB", "kigewellofer", "Pickletonor", "musereyed", "jtcetwerry", "favemallirygexi", "Boissia", "Mus1k", "13TxNA", "Malkies", "IzMessist", "ncKRea99", "WurdRedditAsss", "woodilsmube", "Bastegios", "primp-teruga", "pictupersiari", "V0gnut", "murpingis", "jeiiko382", "Del1GKLU", "UbaBettLiceOffall", "charlo_ele_earl", "shawkzoo", "ShwahwlRordan", "NAGGRADZL", "Galeza00073", "LevhigurouX", "Bowand109", "pendyhapmanbus", "swag1c3", "buggy_toastle0sacchizle", "onlondeach", "illinvonander", "Littleuser_T", "Alacouspearthelecter", "kutatad", "GoodofTheBooFny", "Robong1091", "sipspan", "_Flossell_pool_423ii", "ThatItuwarz", "moashmandeetree", "HandKictionECJ", "Beverd", "slazhaus", "akoxionn", "GamisWorksAtWhorie", "Phhosho12", "A_Dendito10", "dillatablapates", "Acculids", "the_john613", "Leman", "PM_FlomSidel", "Elphawin565", "nuiventr15", "ScottHyupor", "za36", "RewVaye04", "ruster4tyer", "binhan80", "randward98", "parthanuer", "Rimvkul", "beyraidarksoufs", "NipBonetPoombooter", "d-guy", "lightho", "snookskirs", "pilmonster1312", "hoshvorgersus", "lickalivitiko", "DudelScosoin", "shenboomuplow", "moastboy8", "cantremichele", "Chester56", "Inyhemant", "ekndwyguh", "Ginfadiots", "The_Ekennath", "Axyos", "funkzcutes20", "Elpha99", "thabummy", "Gend1x69", "Repra_RuyOHD", "Tineas_Messuous", "SagaFless", "NipplesDevnooon", "datt_fire", "YaveCripstwide", "electrur", "TiCodolibal", "ceniColomentin", "LecuranUnMexo", "umskv", "timp", "Nello", "nokobau", "Frosty_Parted_", "counter4laf", "reguyy500", "MCKOrnnia", "skomone32", "_Plockstorm-Sheep", "S00ban", "iskrawn01", "WTc71129", "jebbberb", "neotherdrawof", "yooohbeatan", "fuckb", "johneysell", "Halltyphold", "Metrikkk", "EMem14", "SM3Weirs", "bb2kton", "dust_daubo21", "ihaisit", "Grann9993", "probsterimis_nife", "GHITORU", "neldmandaway", "YoeeeProltheconnigato", "RebelOpsToAND", "PM-ME_UME_", "orymazora", "Scap-Mades", "190snight0", "sci-some", "ribelyfres", "solatig", "timelainowshoke", "tikelsgoot", "IsscaBen", "inculqeano", "Prodong", "amponcer17", "KnackWilly", "wringpoles", "RolsecSoastBoy", "Kalkon", "Tanophero", "Rain23eekSeven", "nyingknighta", "gammerange12", "AnonymistahPrince", "OPZOT89", "Hylloxx", "Wafkykinsober", "MT04", "redafripple", "S3icatsGigate", "freepand99", "Grashday", "missu_azulawolf", "YourPoraCole667", "UdeoWoodMigemon", "TheRoochy", "azzzia", "ShackleThisLo89", "idont", "neidjiff", "WO94L", "tagey12", "Ragic0s", "J103453", "my3c_Zass", "sirmghgun", "Toodz4019", "the_greeskill", "MBB", "unyexdneekey", "bluntage", "sfhsyctorn", "CoastJay", "Dootsfarty", "alexano1219", "i-en_defaves", "theProz", "sinbull-", "filling-creost", "bragopquachry", "Jazal0000", "darkchance", "_N_B_Boob", "xatman987s", "JustBZ", "blubesgoundi", "defficepluths", "floudy", "Kjayare", "the_evyforration", "Nomon", "tolencel101", "WolfRoility", "BAODG3SMS1", "No_Om_Mudion", "robron84", "MrAndrewr22", "ntmac", "kaitekroad", "kd_legarsom", "Floriteco77", "tentonpoon", "Miggal_Cable_Billia", "amerous", "Scruciory", "GlessDarDodyy", "Night", "chemmer", "shuggler", "brahena725", "Invitaploth", "the_no_bors_urranon", "Sevenups", "BenousSocks", "Vuck_Sc", "whatofthangg", "myma_fread-bast", "olden-", "BlackMyCat", "ancheek", "Angonic99", "petero9298", "eriaganusBaw", "DerADCaron", "MOG40R", "thenamenamemy", "eciduillibe", "Ivanelander", "VOs1010", "bibster", "unJamRak", "Ineor82", "grodyoura", "Fnayson", "serpango", "jefffinitic", "Slipstrovert", "Cynca_qobRover", "NSB", "joo_svy", "JaxHaum", "ikoohehon", "alexnicko", "x-rooooooo", "Samroj427", "Vogalbowhy_quejr", "Joinscootare", "thefunglellehIIGEAWHOSg", "xolbet019", "Thuzwautin", "SfanSome84", "The_Reding", "GretsMC", "marvybolled", "CuntaonousONF", "AborsingLine", "noncurcaka", "Ronrigo", "Silyxarollyhooks", "nikn07", "Danter12323", "guzadi22", "CollRyclord", "Animyindavade", "aleximous", "Yonado_13", "el0xian", "wildy_", "chill300", "phugofhewnbrowpopgaster", "Icee_Canutin", "powentattnat", "kittkiesploxe", "vulparthons", "Irchreete", "Choogehd", "DestricDrugzer", "hackey11312", "Captain", "cjcras", "pora_cogs", "googwismciscuatak", "bigeouru14", "NolatartsBottle", "csstningophuh", "Hyvtas1999", "ZeroAutank", "Mr_Pooknagn", "jcgmanl2", "FattyKillDad", "Bubbenmaagoo", "Ak3302zhult", "Cokbertaker", "BillBuck-Trole", "Phutidnuck_Fon", "firveterTostyMore8999", "frontiploll", "chelsenstrure98", "ruster69", "PleseRuster", "kyler_boaghersy", "CaptianYaustpann", "Callsmadfra21d", "PaddyDudbalrjimary", "aremasannicage", "Nakaru_Triccirist", "fighwicker", "Londsharker", "jewfless", "vorgosm88", "Giscutes", "Onevilleover", "schuattledobro", "Mattmetrace", "spootzetincho", "mooseeks", "cb4lets", "hapmuthanbannard", "1-Pandy", "ACTRobh", "mony-__briX47", "Lustaksnames7588", "Methershatz", "EarlyCesteScempLowon", "Sneezer_Uter-Extouline", "neachuarran", "HoodiestAard", "wherrit", "Ecctt", "lateyfield", "Redsar", "MrBoom654", "onix53", "Tacoh6998", "paomb116", "swangni_s", "abosmonen", "winker_jali", "TheNourTlays7", "MoverphilsInXIrah", "gellyatherewightessuve", "bvansleptons", "thebarbertmacgor", "numiousechumbeep", "alluperriy", "TheReafix34", "bore11703", "drajorjam", "wwmoster", "Marchin2492", "Lams_JerkyIsAssamhs", "Pinegensky", "Dynackonitus", "prp", "FasetsNosque", "mikebaninua", "iasykw", "dedoldkrik", "Tamm107", "craptow", "RoZakersJakes", "MuchanGiwa", "rickcuppinbro", "ghaismin", "darkdackmiss", "BostFuckGram", "Skurd", "willflzbey8", "Fortheom-Rebelgus", "BlueRatt", "JastyNou", "jascule", "DiamloustardosAnd", "Inter_Heat", "well_of_raine_3s", "SqV141", "Golf24", "ShitvirusAcYaFrand", "murdysdepherd", "throwawaysac", "drsnam", "MaxyMrDoverthent", "MrTheArgrus", "Anthospheck", "blurojr52", "no3ll", "Sixbubblespunk", "martiangy", "hyl-crack", "hustinflifk_2n", "theelasamnwehnnif", "marchiefactgetes620", "Joeper1", "DumbieJarkmatts", "narchin717", "Panmaze_terpyro707", "holkmacl11", "Spoonu19", "sissmex", "Whotaki-coders_lip", "wispamus", "CoMaNiseSomeGuy", "numpcxcoust", "ZelMcNotASmilkrons", "APieFmestuper", "Pacrocurce", "vorkingmegold", "Shsmora", "urnump_whitepanta", "36sochiin", "SunquackBlacket", "druterent", "Dictor_Hellmejeevern", "iZonon", "farks40411", "hampthenex", "JustyBigbamti1", "DankoLong", "holy_syct", "meansactblass", "XadianBterler", "nerdmic", "casab57", "killbij", "athidelko_a", "AathicalElefInfless", "8v0egor", "YRFUEShoRo", "SuperWoffeen", "fjlani", "Jeuklebraw", "Paffusit_Winad", "Leu169", "seyfurliteo", "Bwillbeow679", "immorkenceing766", "dramoo0", "nfcwarcle13", "Neubla210", "xisd1104", "CaptHomeprace", "DirtyLobsty", "Epicaprottuck", "_Ithernable_Butt", "Failbropter", "imfanicku", "mether_a-swork", "axstorm", "dern0l801", "StxL53017", "shadowpoopperq8", "DCFhatz", "harmflack95", "manoftamp", "ctec4lt_cuck61", "TirstyTheNewsUne", "OrmonDuu327", "ottlenate_villy", "We_Heve", "Badjak", "TrinsandSope", "a__lakid", "ponkeraldeein_meme", "deckanielkx", "B29thd", "peasernuts512", "ahr1057", "LareaPellowIsPoed", "Williotier", "Homeusme", "DaBloidBodd", "thr2subse", "Dunhma792", "KitterDaybasor", "xoehaero", "jackeyman", "HinderSophes", "mellusdock", "PookeBuddos", "pleepanta", "BroveinHoweOf", "blacal_puggin", "black75031", "MrBristyMan", "wc0nnint6u", "BackNeweeu", "Vher_ewch42", "Feriqlien", "DrDuvyDovmai", "cobiemb095", "Yatdoat", "Tap_Of_Dast", "sterricus", "ArixCrodus", "TusleFw", "irizashi3", "LefoVicgust", "Chrischristinuspwords", "Stepifesbick11", "hardongapper", "quatohatpeedle", "Seeyo", "boy_pinalta", "vaint", "consisterlamati", "Wheelyfehawkv", "phelsey629", "apstarshats", "illegenaripe", "TurtlesBoyOfLegitMic", "ferigia", "avamistellyhouse", "abulley-pleun", "FireniumNumz", "alexxxjunces", "Brginmy", "DistleTimeFlower", "DegoG", "themillmacreddit", "Quinsteactus2", "illman", "TavaVilla", "Afkeringhaps", "NeGinic", "hokee_293", "anzhude", "Feniucastiny", "bubyfutch", "darkmaturapen", "marknew", "somtej7", "Sredkprinta", "aargymikea1712", "TheSchDerton", "jellokp", "yourworksandsoul3", "grimlies", "WareBando", "leaddish", "becobrade", "TheVenibleSRussides", "MC3_0", "cbrukhhrn", "Drapiz", "O_da1-1014", "wigertoon12", "SeroSardai", "ST1130", "Claceballder", "bkwfkx", "Azilux", "stevelockmarklin", "Zauriand275", "yqq905", "smickys", "larieven", "NotIMCrase", "NuckyPuff", "livius", "kreak201j66", "Asiasimean7", "xbob", "Trates_Reddit", "goncops", "LaurTheDaInA", "camiarly", "-cat_space", "Geeee", "ANonsmeEY", "unconettevis_2cimtad", "0n_awom", "BasileKeet", "parsettins", "jksl", "thumpsuberkamabig", "DixaganHeil", "DRsMireJusHornson", "RestryRailliber", "plamacejuck89", "BubFace", "evelio", "UronickThans", "waskammanreabe", "MarkColomballs", "Eq_Cntect_", "gomereeshposter", "Jeneethity1", "TagouseNotS33", "Fryson11987", "grimboecoverder2699", "v8gjess", "SilonBeMoungAtCovans", "DimeyLaizz", "racagentargud", "Beeldang5", "trabligars", "eldreamblans", "Cweccolix", "OS_LG-On_Your-OSPreamy", "L3ERVMRClauch", "snecksidk4tatho", "Dragonvis_Bang", "Wolfleklike3", "Ravan8une", "hourschoesconstine", "felfkson", "BranendSurtles", "therephoan", "Locre_Muggot", "Yough", "I_sevent_where", "truswatern", "Tweezer", "kalado217", "cogoffarst", "cuiconfourcat", "gah_davel_misher", "Ikovanofuar", "relecucal", "wellball13", "SyrofSdividhandz", "villerc15", "HamateCresis", "L3jaMWF", "General", "rossintqred", "bl3epstickant", "MamBee94263", "ThatPharonmakers", "Keugeemovi", "Havr", "GeroriusCan", "MitchPixh3c", "Shepst", "seyyyal", "deffin423", "MDeather", "TravaWorkRankey", "Pineaurous779", "rannaasf", "aurothese", "ohnnnmid", "stuckingthetomestecado", "Musernapfallbill", "rankzn121", "Alperjanoo", "FotAlwyl13", "incomnympreid", "KLL337", "Vines_7_Choose", "mksheel42", "coctoma27", "Phallin-Have", "angeracfromial", "MESOSDAGT2R", "LuttyI113", "NillyP4", "nehoodnow", "ierieze", "Saicea", "Rafusern", "ts323", "Ming_Scht", "mmedsparten", "Leganis_Smarks", "Sol_OfHaeds", "omwelvenami", "tockless390", "WhighDittaframPantdo1", "develusinthe", "xx", "dcayskor", "callmectiredway", "Ball_SigS", "SurtaTanHab", "chucketinsponker", "Kastheredh", "lathyforvolius", "1_002", "ascel", "daughwyndrs", "Jack_Wolf_O_A-", "zroob9", "brizz_dright_westabee", "gotchloghnarts", "AarloJast", "pik7h023", "NT2CONE", "dbenaterodacalos", "Djon77", "AdiePajes", "NuxFiran", "McZabeem", "AmpernGore", "Rogerpow", "mvnjerk", "0lastbngfXm", "bnandens4", "meejsorke", "sybaiyanschrosalaz", "Steed3Food", "ecooldgiests", "JM517", "gigerssapchill", "cashkeemer", "Tr0ct_It_third", "tz2citts000", "folofruccing", "Monjcagu", "cdenigrime", "syvenalatt217", "aleccant60016", "MyPrankerPerusfis", "blathbachine", "flyep41", "StoclansFaxUd", "BrienPsqA86", "not_dehurs", "Tommymeyiwoon", "thefindne", "Loupannic4len08", "HD25", "TheMarthGovrsky", "wiggiegopotis", "rabb412", "TheNyaftfia", "Whitgling_offii", "bllr-a_cool", "ArvalhelGuy", "daingding1973", "Pomechar", "Mongai_", "missiemra", "xDxD-fox", "adizard_tbaon", "heogrollate", "ABL0s2ost", "Sasio", "Galballw", "youroedor", "Shempster", "rkkhj", "anjoha_pulada69", "FooFealQuarrewu", "caleandsaured", "2chun2093", "ITMEG152", "rbruce3400", "JawComptin", "Fuckburdersarch", "slostarp", "fulfusetha", "forfali", "peagj__", "blacks_jmunku", "arother_tuigball", "budso", "aFunpot", "msblurdiesxkr0", "ljoney_in_mal", "toopisug", "ThatBlorrapen", "fark_ash98", "jjogee1099", "Shoonmaded", "lomanvivancia", "2namesmanadava", "ATombai", "thrispnessly", "Apixterius", "iamphis222", "Cheekerocks", "Notsekresties", "Hustro16", "ia84", "Happerz0", "welledstarzen", "serabod", "Sanadhear13", "NaughtLo", "prainwavev", "hique1", "twolly14", "T1RScuntystrokaes", "hayuera", "ab5rai308", "MaveDexic84", "crath_biber111", "workant_rynj", "FMOMSFSAT", "boydrybromer", "Goldersa", "TheSdifsDrm", "introll131", "neonz510", "BagLamnet", "muthywalker", "drinksboggect", "proboomslition", "eric_on_niwin", "drimeta4908", "MikaShrough", "kncdino400", "tringomutre", "GjoshiBeamt", "GoldensPeeper04", "langas01Frophes", "AmesGal", "YomemBlathR", "ane28", "Spickes_I", "Anoulo78", "JAnch-", "OnfikinEitintos", "Booty60", "Siding_4_T", "imoneverybuts", "CrondraPuribu", "P394HutChizPrn", "GomphinTV", "Jupist99", "big311", "Metrescho-Yangonncof-skin", "cashpop567", "adam_afrunking", "murkeyxworld", "SpeedyBrozatie", "jjmc", "OnOKcom", "Asti_Taco", "Serchieto", "Storg", "carchingtah", "rellionicaturn100", "AlikenSandles", "Phoster_Chestrax", "gun_b", "thommessocuust", "RudenDubsu", "whinewil375", "TheDikekidukilrain", "TotSelBuck", "thebaobowbodgy", "xvOreNugeHewf", "dandastom", "irishgrey", "AbraceHomoStretchbroe", "SteDarLahbell", "TeeRanky", "kekman123", "EpalisPhace482", "speecoespana", "martin_the-trd_Butt", "Hyekin_", "siriousleapy", "Engmorex", "shangeredk65", "HelfSteacyphopeon", "DRLM_FLOS_", "Tunderpice90", "Reforthesal", "qwackporder", "BuddissClouAmAbay", "ShallsShite", "HoDahghunnet1983", "Higroom", "TRSSHITER", "captainSlips", "Mrspankles"};

	@Override
	public void startUp()
	{
// 		fakeRsn = randomAlphaNumeric(12);

Random generator = new Random();
int randomIndex = generator.nextInt(NAMES.length);
fakeRsn = NAMES[randomIndex];
	}

	@Override
	public void shutDown()
	{
		clientThread.invokeLater(() -> client.runScript(ScriptID.CHAT_PROMPT_INIT));
	}

	@Subscribe
	private void onBeforeRender(BeforeRender event)
	{
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return;
		}

		if (config.hideWidgets())
		{
			// do every widget
			for (Widget widgetRoot : client.getWidgetRoots())
			{
				processWidget(widgetRoot);
			}
		}
		else
		{
			// just do the chatbox
			updateChatbox();
		}
	}

	/**
	 * Recursively traverses widgets looking for text containing the players name, replacing it if necessary
	 * @param widget The root widget to process
	 */
	private void processWidget(Widget widget)
	{
		if (widget == null)
		{
			return;
		}

		if (widget.getText() != null)
		{
			widget.setText(replaceRsn(widget.getText()));
		}

		for (Widget child : widget.getStaticChildren())
		{
			processWidget(child);
		}

		for (Widget dynamicChild : widget.getDynamicChildren())
		{
			processWidget(dynamicChild);
		}

		for (Widget nestedChild : widget.getNestedChildren())
		{
			processWidget(nestedChild);
		}
	}

	private void updateChatbox()
	{
		Widget chatboxTypedText = client.getWidget(WidgetInfo.CHATBOX_INPUT);
		if (chatboxTypedText == null || chatboxTypedText.isHidden())
		{
			return;
		}
		String[] chatbox = chatboxTypedText.getText().split(":", 2);

		//noinspection ConstantConditions
		String playerRsn = Text.toJagexName(client.getLocalPlayer().getName());
		if (Text.standardize(chatbox[0]).contains(Text.standardize(playerRsn)))
		{
			chatbox[0] = fakeRsn;
		}

		chatboxTypedText.setText(chatbox[0] + ":" + chatbox[1]);
	}

	@Subscribe
	private void onChatMessage(ChatMessage event)
	{
		//noinspection ConstantConditions
		if (client.getLocalPlayer().getName() == null)
		{
			return;
		}

		String replaced = replaceRsn(event.getMessage());
		event.setMessage(replaced);
		event.getMessageNode().setValue(replaced);

		if (event.getName() == null)
		{
			return;
		}

		boolean isLocalPlayer =
			Text.standardize(event.getName()).equalsIgnoreCase(Text.standardize(client.getLocalPlayer().getName()));

		if (isLocalPlayer)
		{
			event.setName(fakeRsn);
			event.getMessageNode().setName(fakeRsn);
		}
	}

	@Subscribe
	private void onOverheadTextChanged(OverheadTextChanged event)
	{
		event.getActor().setOverheadText(replaceRsn(event.getOverheadText()));
	}

	private String replaceRsn(String textIn)
	{
		//noinspection ConstantConditions
		String playerRsn = Text.toJagexName(client.getLocalPlayer().getName());
		String standardized = Text.standardize(playerRsn);
		while (Text.standardize(textIn).contains(standardized))
		{
			int idx = textIn.replace("\u00A0", " ").toLowerCase().indexOf(playerRsn.toLowerCase());
			int length = playerRsn.length();
			String partOne = textIn.substring(0, idx);
			String partTwo = textIn.substring(idx + length);
			textIn = partOne + fakeRsn + partTwo;
		}
		return textIn;
	}

	private static String randomAlphaNumeric(int count)
	{
		StringBuilder builder = new StringBuilder();
		int i = count;
		while (i-- != 0)
		{
			int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
			builder.append(ALPHA_NUMERIC_STRING.charAt(character));
		}
		return builder.toString();
	}
}
