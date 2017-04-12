package com.rothen.rbtimer.model;

import android.content.Context;
import android.content.res.Resources;

import com.rothen.rbtimer.R;
import com.rothen.rbtimer.service.SettingsService;

import java.util.ArrayList;
import java.util.List;

/**
 * manage parameters for the Bomb activity
 * Created by apest on 02/04/2017.
 */

public class BombSettingsParameters implements IActivitySettingsParameters {
    public static final String BOMBSETTINGS = "BOMBSETTINGS";

    public static final String CAT_ACCELEROMETERS = "CAT_ACCELEROMETERS";
    public static final String PAR_ACCELEROMETERS_CT = CAT_ACCELEROMETERS + "PAR_ACCELEROMETERS_CT";
    public static final String PAR_ACCELEROMETERS_T = CAT_ACCELEROMETERS + "PAR_ACCELEROMETERS_T";

    public static final String CAT_BOMB = "CAT_BOMB";
    public static final String PAR_BOMB_DURATION= CAT_BOMB + "PAR_BOMB_DURATION";
    public static final String PAR_BOMB_ARM_DURATION= CAT_BOMB + "PAR_BOMB_ARM_DURATION";
    public static final String PAR_BOMB_DISARM_DURATION= CAT_BOMB + "PAR_BOMB_DISARM_DURATION";
    public static final String PAR_BOMB_DEFUSEKIT_FACTOR= CAT_BOMB + "PAR_BOMB_DEFUSEKIT_FACTOR";
    public static final String PAR_BOMB_SLOW_BIP_DURATION= CAT_BOMB + "PAR_BOMB_SLOW_BIP_DURATION";
    public static final String PAR_BOMB_FAST_BIP_DURATION= CAT_BOMB + "PAR_BOMB_FAST_BIP_DURATION";

    public static final String CAT_NFC = "CAT_NFC";
    public static final String PAR_NFC_ISENABLED= CAT_BOMB + "PAR_NFC_ISENABLED";
    public static final String PAR_NFC_BOMBDROPZONETAG= CAT_BOMB + "PAR_NFC_BOMBDROPZONETAG";
    public static final String PAR_NFC_DEFUSETAG= CAT_BOMB + "PAR_NFC_DEFUSETAG";

    private List<SettingsCategory> listSettings;

    public BombSettingsParameters(Context context) {
        Resources resources = context.getResources();
        String title;
        listSettings = new ArrayList<>();

        SettingsCategory nfc = new SettingsCategory();
        nfc.setCategoryTitle(resources.getString(R.string.settings_NFC));
        nfc.setCategoryId(CAT_NFC);
        title = resources.getString(R.string.settings_NFC_IsEnabled);
        nfc.addParameter(new SettingsParameter(
                title,
                PAR_NFC_ISENABLED,
                false,
                true,
                SettingsParameterType.TYPE_BOOLEAN
        ));
        title = resources.getString(R.string.settings_NFC_BombDropZoneTag);
        nfc.addParameter(new SettingsParameter(
                title,
                PAR_NFC_BOMBDROPZONETAG,
                "",
                "",
                SettingsParameterType.TYPE_STRING
        ));
        title = resources.getString(R.string.settings_NFC_DefuseTag);
        nfc.addParameter(new SettingsParameter(
                title,
                PAR_NFC_DEFUSETAG,
                "",
                "",
                SettingsParameterType.TYPE_STRING
        ));

        listSettings.add(nfc);

        SettingsCategory accelerometers = new SettingsCategory();
        accelerometers.setCategoryTitle(resources.getString(R.string.settings_accelerometers));
        accelerometers.setCategoryId(CAT_ACCELEROMETERS);
        title = resources.getString(R.string.settings_accelerometers_CT);
        accelerometers.addParameter(new SettingsParameter(
                title,
                PAR_ACCELEROMETERS_CT,
                0f,
                1f,
                SettingsParameterType.TYPE_FLOAT
        ));
        title = resources.getString(R.string.settings_accelerometers_T);
        accelerometers.addParameter(new SettingsParameter(
                title,
                PAR_ACCELEROMETERS_T,
                0f,
                1f,
                SettingsParameterType.TYPE_FLOAT
        ));
        listSettings.add(accelerometers);

        SettingsCategory bomb = new SettingsCategory();
        bomb.setCategoryTitle(resources.getString(R.string.settings_Bomb));
        bomb.setCategoryId(CAT_BOMB);
        title = resources.getString(R.string.settings_Bomb_duration);
        bomb.addParameter(new SettingsParameter(
                title,
                PAR_BOMB_DURATION,
                0,
                300,
                SettingsParameterType.TYPE_INT
        ));
        title = resources.getString(R.string.settings_Bomb_armDuration);
        bomb.addParameter(new SettingsParameter(
                title,
                PAR_BOMB_ARM_DURATION,
                0,
                60,
                SettingsParameterType.TYPE_INT
        ));
        title = resources.getString(R.string.settings_Bomb_disarmDuration);
        bomb.addParameter(new SettingsParameter(
                title,
                PAR_BOMB_DISARM_DURATION,
                0,
                120,
                SettingsParameterType.TYPE_INT
        ));
        title = resources.getString(R.string.settings_Bomb_defuseKitFactor);
        bomb.addParameter(new SettingsParameter(
                title,
                PAR_BOMB_DEFUSEKIT_FACTOR,
                0,
                4,
                SettingsParameterType.TYPE_INT
        ));
        title = resources.getString(R.string.settings_Bomb_delaySowBip);
        bomb.addParameter(new SettingsParameter(
                title,
                PAR_BOMB_SLOW_BIP_DURATION,
                0,
                120,
                SettingsParameterType.TYPE_INT
        ));
        title = resources.getString(R.string.settings_Bomb_delayFastBip);
        bomb.addParameter(new SettingsParameter(
                title,
                PAR_BOMB_FAST_BIP_DURATION,
                0,
                60,
                SettingsParameterType.TYPE_INT
        ));
        listSettings.add(bomb);


    }

    @Override
    public List<SettingsCategory> getCategories() {
        return listSettings;
    }
}
