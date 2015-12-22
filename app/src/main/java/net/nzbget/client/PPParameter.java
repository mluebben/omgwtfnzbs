package net.nzbget.client;

/**
 * Post-processing parameter.
 *
 * @author mluebben
 */
public class PPParameter {

    private String mName;
    private String mValue;

    public PPParameter(String name, String value) {
        mName = name;
        mValue = value;
    }

    public String getName() {
        return mName;
    }

    public String getValue() {
        return mValue;
    }
}
