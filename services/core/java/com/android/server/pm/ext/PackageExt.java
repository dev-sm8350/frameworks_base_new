package com.android.server.pm.ext;

import android.ext.AppInfoExt;
import android.ext.PackageId;
import android.os.Parcel;

import com.android.internal.pm.parsing.pkg.PackageExtIface;
import com.android.internal.pm.parsing.pkg.PackageImpl;
import com.android.server.pm.pkg.AndroidPackage;

public class PackageExt implements PackageExtIface {
    public static final PackageExt DEFAULT = new PackageExt(PackageId.UNKNOWN, 0);

    private final int packageId;
    private final int flags;

    private final PackageHooks hooks;

    public static PackageExt get(AndroidPackage pkg) {
        PackageExtIface i = pkg.ext();
        if (i instanceof PackageExt) {
            return (PackageExt) i;
        }
        return DEFAULT;
    }

    public PackageExt(int packageId, int flags) {
        this.packageId = packageId;
        this.flags = flags;
        this.hooks = PackageHooksRegistry.getHooks(packageId);
    }

    public int getPackageId() {
        return packageId;
    }

    public PackageHooks hooks() {
        return hooks;
    }

    public AppInfoExt toAppInfoExt(PackageImpl pkg) {

	if (this == DEFAULT) {
            return AppInfoExt.DEFAULT;
        }

        return new AppInfoExt(packageId, flags);
    }

    public void writeToParcel(Parcel dest) {
        boolean def = this == DEFAULT;
        dest.writeBoolean(def);
        if (def) {
            return;
        }
        dest.writeInt(this.packageId);
        dest.writeInt(this.flags);
    }

    public static PackageExt createFromParcel(PackageImpl pkg, Parcel p) {
        if (p.readBoolean()) {
            return DEFAULT;
        }
        return new PackageExt(p.readInt(), p.readInt());
    }
}
