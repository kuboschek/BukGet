package is.hw.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Muss vor Content Felder in einem BukGetData gesetzt werden, wenn Daten dort rein deserialisiert werden sollen.
 * @author simon
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonWebContent {

}
