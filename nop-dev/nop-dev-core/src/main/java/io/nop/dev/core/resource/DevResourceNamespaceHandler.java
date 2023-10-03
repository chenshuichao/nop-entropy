package io.nop.dev.core.resource;

import io.nop.api.core.util.Guard;
import io.nop.commons.util.FileHelper;
import io.nop.commons.util.StringHelper;
import io.nop.core.resource.IResource;
import io.nop.core.resource.IResourceNamespaceHandler;
import io.nop.core.resource.IResourceStore;
import io.nop.core.resource.impl.FileResource;
import io.nop.dev.core.DevCoreConstants;

import java.io.File;

import static io.nop.dev.core.DevCoreConfigs.CFG_DEV_ROOT_PATH;

public class DevResourceNamespaceHandler implements IResourceNamespaceHandler {
    public static DevResourceNamespaceHandler INSTANCE = new DevResourceNamespaceHandler();

    @Override
    public String getNamespace() {
        return DevCoreConstants.RESOURCE_NS_DEV;
    }

    @Override
    public IResource getResource(String path, IResourceStore locator) {
        Guard.checkArgument(StringHelper.startsWithNamespace(path, DevCoreConstants.RESOURCE_NS_DEV), "path must startsWith dev:");
        Guard.checkArgument(!path.contains(".."), "invalid path");

        String rootPath = CFG_DEV_ROOT_PATH.get();
        if (StringHelper.isEmpty(rootPath)) {
            rootPath = FileHelper.currentDir().getAbsolutePath();
        }

        String devPath = path.substring(DevCoreConstants.RESOURCE_NS_DEV.length() + 1);

        File file = new File(rootPath, devPath);
        return new FileResource(path, file);
    }
}