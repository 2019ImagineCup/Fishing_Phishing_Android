package ensharp.imagincup2019.fishingphishing.Common.Module;

import ensharp.imagincup2019.fishingphishing.Common.Model.CallHistory;
import ensharp.imagincup2019.fishingphishing.Common.Model.CurrentCallDate;
import io.realm.annotations.RealmModule;

@RealmModule(classes = {CallHistory.class, CurrentCallDate.class})
public class CallHistoryModule {
}
