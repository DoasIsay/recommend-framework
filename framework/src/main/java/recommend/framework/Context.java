package recommend.framework;

import lombok.Data;

@Data
public class Context {
    int offset;
    int size;
    String city="";
    String version="";
    String uid="";
    String os ="";
}
