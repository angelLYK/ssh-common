package ml.rugal.sshcommon.hibernate;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the updater class for updating entity.
 *
 * @author Rugal Bernstein
 * @param <T> target entity class.
 */
public class Updater<T>
{

    /**
     * Constructor of a updater, with default update mode.
     *
     * @param bean The bean to be updated on.
     */
    public Updater(T bean)
    {
        this.bean = bean;
    }

    /**
     * Constructor of a updater.
     *
     * @param bean entity bean to be updated.
     * @param mode update mode in later operation.
     */
    public Updater(T bean, UpdateMode mode)
    {
        this.bean = bean;
        this.mode = mode;
    }

    public Updater<T> setUpdateMode(UpdateMode mode)
    {
        this.mode = mode;
        return this;
    }

    /**
     * Add property for includes.
     *
     * @param property property name
     * <p>
     * @return current updater
     */
    public Updater<T> include(String property)
    {
        includeProperties.add(property);
        return this;
    }

    /**
     * Add property for excludes.
     *
     * @param property The property to be excluded.
     * <p>
     * @return current updater
     */
    public Updater<T> exclude(String property)
    {
        excludeProperties.add(property);
        return this;
    }

    /**
     * Check a property or a field needs update.<BR>
     * 1. In MAX mode will update properties that is not excluded<BR>
     * 2. In MIN mode will update properties that is included<BR>
     * 3. In MIDDLE mode will update properties that is value either null but
     * included, or not null but not excluded.
     *
     * @param name  The property to be checked.
     * @param value null value will be evaluated.
     * <p>
     * @return Judge whether a property needs to update.
     */
    public boolean isUpdate(String name, Object value)
    {
        boolean needUpdate = false;
        if (this.mode == UpdateMode.MAX)
        {
            needUpdate = !excludeProperties.contains(name);
        }
        else if (this.mode == UpdateMode.MIN)
        {
            needUpdate = includeProperties.contains(name);
        }
        else if (this.mode == UpdateMode.MIDDLE)
        {
            if (value != null)
            {
                needUpdate = !excludeProperties.contains(name);
            }
            else
            {
                needUpdate = includeProperties.contains(name);
            }
        }
        return needUpdate;
    }

    private final T bean;

    private final Set<String> includeProperties = new HashSet<>();

    private final Set<String> excludeProperties = new HashSet<>();

    private UpdateMode mode = UpdateMode.MIDDLE;

    private static final Logger log = LoggerFactory.getLogger(Updater.class);

    public static enum UpdateMode
    {

        MAX, MIN, MIDDLE
    }

    public T getBean()
    {
        return bean;
    }

    public Set<String> getExcludeProperties()
    {
        return excludeProperties;
    }

    public Set<String> getIncludeProperties()
    {
        return includeProperties;
    }
}
