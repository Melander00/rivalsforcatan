package samuel.test.common;

import samuel.resource.Resource;
import samuel.resource.ResourceBundle;

import java.util.Arrays;

public class ResourceBundleHelper {

    @SafeVarargs
    public static ResourceBundle createBundle(Class<? extends Resource>... resources) {
        ResourceBundle bundle = new ResourceBundle();

        Arrays.stream(resources).forEach(res -> bundle.addResource(res, 1));

        return bundle;
    }

}
