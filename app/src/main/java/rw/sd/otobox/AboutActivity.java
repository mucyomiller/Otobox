package rw.sd.otobox;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.widget.Toast;

import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItemOnClickAction;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.danielstone.materialaboutlibrary.util.OpenSourceLicense;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.orhanobut.hawk.Hawk;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

import bolts.Task;


public class AboutActivity extends MaterialAboutActivity {
    private static final String TAG = "AboutActivity";
    public static final String THEME_EXTRA = "";
    public static final int THEME_LIGHT_LIGHTBAR = 0;
    public static final int THEME_LIGHT_DARKBAR = 1;
    public static final int THEME_DARK_LIGHTBAR = 2;
    public static final int THEME_DARK_DARKBAR = 3;
    public static final int THEME_CUSTOM_CARDVIEW = 4;
    protected int colorIcon = R.color.mal_color_icon_light_theme;
    public boolean loaded = false;
    public String website = "http://otobox.com";
    public String phone;
    public String email;
    public String latitude;
    public String longitude;
    ArrayList<String> mAboutInfo;
    @NonNull
    @Override
    protected MaterialAboutList getMaterialAboutList(@NonNull Context c) {
        mAboutInfo = new ArrayList<>();
        //mAboutInfo DATA is retrieved from MainActivity
        if(Hawk.contains("mAboutInfo")){
            loaded = true;
            mAboutInfo = Hawk.get("mAboutInfo");
            website = mAboutInfo.get(0);
            phone   = mAboutInfo.get(1);
            email   = mAboutInfo.get(2);
            latitude = mAboutInfo.get(3);
            longitude = mAboutInfo.get(4);
        }

        MaterialAboutCard.Builder appCardBuilder = new MaterialAboutCard.Builder();
        // Add items to card
        appCardBuilder.addItem(new MaterialAboutTitleItem.Builder()
                .text("Otobox")
                .desc("© 2017 sd.rw")
                .icon(R.mipmap.ic_launcher)
                .build());

        appCardBuilder.addItem(ConvenienceBuilder.createVersionActionItem(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_info_outline)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Version",
                false));

        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Changelog")
                .icon(new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_history)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .setOnClickAction(ConvenienceBuilder.createWebViewDialogOnClickAction(c, "Releases", "<ul><li>Initial Release</li></ul>", false, false))
                .build());

        appCardBuilder.addItem(new MaterialAboutActionItem.Builder()
                .text("Licenses")
                .icon(new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18))
                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                    @Override
                    public void onClick() {
                        Intent intent = new Intent(c, LicenseActivity.class);
                        intent.putExtra(AboutActivity.THEME_EXTRA, getIntent().getIntExtra(THEME_EXTRA, THEME_LIGHT_DARKBAR));
                        c.startActivity(intent);
                    }
                })
                .build());
        //informations
        MaterialAboutCard.Builder convenienceCardBuilder = new MaterialAboutCard.Builder();

        convenienceCardBuilder.title("About Us");

        convenienceCardBuilder.addItem(ConvenienceBuilder.createWebsiteActionItem(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_earth)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Visit Website",
                true,
                Uri.parse(website) ));

        convenienceCardBuilder.addItem(ConvenienceBuilder.createRateActionItem(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_star)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Rate this app",
                null
        ));

        convenienceCardBuilder.addItem(ConvenienceBuilder.createEmailItem(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_email)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Send an email",
                true,
                email,
                "Help About Otobox"));

        convenienceCardBuilder.addItem(ConvenienceBuilder.createPhoneItem(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_phone)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Call Us",
                true,
                phone));
        convenienceCardBuilder.addItem(new MaterialAboutActionItem("Chat With Us","On WhatsApp",new IconicsDrawable(c)
                .icon(CommunityMaterial.Icon.cmd_whatsapp)
                .color(ContextCompat.getColor(c, colorIcon))
                .sizeDp(18),()->{
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_VIEW);

            String url = "https://api.whatsapp.com/send?phone="+ PhoneNumberUtils.stripSeparators(phone);
            sendIntent.setData(Uri.parse(url));
            startActivity(sendIntent);
        }));

        convenienceCardBuilder.addItem(ConvenienceBuilder.createMapItem(c,
                new IconicsDrawable(c)
                        .icon(CommunityMaterial.Icon.cmd_map)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Visit Us",
                null,
                latitude+","+longitude));
            if(loaded){
                return new MaterialAboutList(appCardBuilder.build(), convenienceCardBuilder.build());
            }
        return new MaterialAboutList(appCardBuilder.build());
    }


    public static MaterialAboutList createMaterialAboutLicenseList(final Context c, int colorIcon) {

        MaterialAboutCard rxjavaLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Rxjava", "2016", "RxJava Contributors",
                OpenSourceLicense.APACHE_2);
        MaterialAboutCard rxandroidLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "RxAndroid", "2015", "The RxAndroid authors",
                OpenSourceLicense.APACHE_2);
        MaterialAboutCard okhttpLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "okhttp", "2014", "Square Inc.",
                OpenSourceLicense.APACHE_2);
        MaterialAboutCard rxnetworkLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "rxnetwork", "2016", "Piotr Wittchen",
                OpenSourceLicense.APACHE_2);

        MaterialAboutCard glideLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Glide", "2014", "Sam Judd",
                OpenSourceLicense.APACHE_2);
        MaterialAboutCard androidIconicsLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Android Iconics", "2016", "Mike Penz",
                OpenSourceLicense.APACHE_2);

        MaterialAboutCard leakCanaryLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "LeakCanary", "2015", "Square, Inc",
                OpenSourceLicense.APACHE_2);
        MaterialAboutCard materialAboutLIbraryLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "material-about-library", "2016", "Daniel Stone",
                OpenSourceLicense.APACHE_2);
        MaterialAboutCard MucyoMillerShoppingCartLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "mucyoshoppingcart", "2017", "Mucyo Miller",
                OpenSourceLicense.MIT);

        MaterialAboutCard AndroidParseLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "android-parse", "2017", "parse contributors",
                OpenSourceLicense.MIT);
        MaterialAboutCard SupportDesignLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Android Support Design", "2017", "Google",
                OpenSourceLicense.MIT);

        MaterialAboutCard AndroidSupportLibraryLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Android Support Library", "2017", "Google",
                OpenSourceLicense.MIT);
        MaterialAboutCard NumberPickerLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Number Picker", "2017", "shawnlin",
                OpenSourceLicense.MIT);
        MaterialAboutCard fancybuttonsLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "fancybuttons", "2017", "medyo",
                OpenSourceLicense.MIT);
        MaterialAboutCard lottieLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "lottie", "2017", "AirBnB",
                OpenSourceLicense.MIT);
        MaterialAboutCard rxrelayLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "rxrelay", "2017", "Jake Wharton",
                OpenSourceLicense.MIT);
        MaterialAboutCard rxbindingLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "rxbinding", "2017", "Jake Wharton",
                OpenSourceLicense.MIT);
        MaterialAboutCard loaderviewlibraryLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "loaderviewlibrary", "2017", "elyeproj",
                OpenSourceLicense.MIT);
        MaterialAboutCard gsonLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "gson", "2017", "Google",
                OpenSourceLicense.MIT);
        MaterialAboutCard TriStateToggleButtonLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "TriStateToggleButton", "2017", "BeppiMenozzi",
                OpenSourceLicense.MIT);
        MaterialAboutCard badgecounterLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "badgecounter", "2017", "juanlabrador",
                OpenSourceLicense.MIT);
        MaterialAboutCard ToastyLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "Toasty", "2017", "GrenderG",
                OpenSourceLicense.MIT);
        MaterialAboutCard hawkLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "hawk", "2017", "orhanobut",
                OpenSourceLicense.MIT);
        MaterialAboutCard  materialdialogsLicenseCard = ConvenienceBuilder.createLicenseCard(c,
                new IconicsDrawable(c)
                        .icon(GoogleMaterial.Icon.gmd_book)
                        .color(ContextCompat.getColor(c, colorIcon))
                        .sizeDp(18),
                "material-dialogs", "2017", "afollestad",
                OpenSourceLicense.MIT);


        return new MaterialAboutList(
                rxjavaLicenseCard,
                rxandroidLicenseCard,
                okhttpLicenseCard,
                rxnetworkLicenseCard,
                glideLicenseCard,
                materialAboutLIbraryLicenseCard,
                androidIconicsLicenseCard,
                leakCanaryLicenseCard,
                MucyoMillerShoppingCartLicenseCard,
                AndroidParseLicenseCard,
                SupportDesignLicenseCard,
                AndroidSupportLibraryLicenseCard,
                NumberPickerLicenseCard,
                fancybuttonsLicenseCard,
                lottieLicenseCard,
                rxrelayLicenseCard,
                rxbindingLicenseCard,
                loaderviewlibraryLicenseCard,
                gsonLicenseCard,
                TriStateToggleButtonLicenseCard,
                badgecounterLicenseCard,
                ToastyLicenseCard,
                hawkLicenseCard,
                materialdialogsLicenseCard

        );
    }

    @Nullable
    @Override
    protected CharSequence getActivityTitle() {
        return getString(R.string.mal_title_about);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Log.i("test", "onCreate: " + getIntent().getIntExtra(THEME_EXTRA, THEME_LIGHT_DARKBAR));
        switch (getIntent().getIntExtra(THEME_EXTRA, THEME_LIGHT_DARKBAR)) {
            case THEME_LIGHT_LIGHTBAR:
                setTheme(R.style.AppTheme_MaterialAboutActivity_Light);
                colorIcon = R.color.mal_color_icon_light_theme;
                break;
            case THEME_DARK_LIGHTBAR:
                setTheme(R.style.AppTheme_MaterialAboutActivity_Dark_LightActionBar);
                colorIcon = R.color.mal_color_icon_dark_theme;
                break;
            case THEME_LIGHT_DARKBAR:
                setTheme(R.style.AppTheme_MaterialAboutActivity_Light_DarkActionBar);
                colorIcon = R.color.mal_color_icon_light_theme;
                break;
            case THEME_DARK_DARKBAR:
                setTheme(R.style.AppTheme_MaterialAboutActivity_Dark);
                colorIcon = R.color.mal_color_icon_dark_theme;
                break;
            case THEME_CUSTOM_CARDVIEW:
                setTheme(R.style.AppTheme_MaterialAboutActivity_Light_DarkActionBar_CustomCardView);
                colorIcon = R.color.mal_color_icon_dark_theme;
                break;
        }

        super.onCreate(savedInstanceState);

    }
}
