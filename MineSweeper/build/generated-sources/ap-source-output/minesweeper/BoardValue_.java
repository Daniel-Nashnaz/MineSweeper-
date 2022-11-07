package minesweeper;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import minesweeper.BoardProperties;
import minesweeper.Users;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2022-07-10T18:09:24", comments="EclipseLink-2.7.7.v20200504-rNA")
@StaticMetamodel(BoardValue.class)
public class BoardValue_ { 

    public static volatile SingularAttribute<BoardValue, Integer> columns;
    public static volatile SingularAttribute<BoardValue, Boolean> valueFlag;
    public static volatile SingularAttribute<BoardValue, BoardProperties> boardID;
    public static volatile SingularAttribute<BoardValue, Integer> id;
    public static volatile SingularAttribute<BoardValue, Integer> rows;
    public static volatile SingularAttribute<BoardValue, Integer> valueMine;
    public static volatile SingularAttribute<BoardValue, Users> userID;

}