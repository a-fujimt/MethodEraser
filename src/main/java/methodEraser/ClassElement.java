package methodEraser;

import java.util.List;

public interface ClassElement {

    void erase(String methodName);

    void erase(List<String> noEraseMethods);

    String getContents();

}
