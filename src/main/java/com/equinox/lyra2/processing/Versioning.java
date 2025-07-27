package com.equinox.lyra2.processing;

import com.equinox.lyra2.exceptions.LyraModelLoadingError;
import com.equinox.lyra2.objects.LyraModel;

public class Versioning {
    public static LyraModel updateModel(LyraModel model) {

        //This is a placeholder method for when updates come out to the LyraModel structure, this should be able to update it.

        throw new LyraModelLoadingError("UNKNOWN VERSION");
    }
}

//This class will manage updating models from an older version of Lyra2. Right now it is empty,
//since there have not been any updates.