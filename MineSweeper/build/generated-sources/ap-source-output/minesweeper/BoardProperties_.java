package minesweeper;

import javax.annotation.processing.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import minesweeper.BoardValue;
import minesweeper.Users;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2022-07-10T18:09:24", comments="EclipseLink-2.7.7.v20200504-rNA")
@StaticMetamodel(BoardProperties.class)
public class BoardProperties_ { 

    public static volatile SingularAttribute<BoardProperties, Integer> sizeCol;
    public static volatile SingularAttribute<BoardProperties, Integer> sizeRow;
    public static volatile SingularAttribute<BoardProperties, Integer> boardID;
    public static volatile CollectionAttribute<BoardProperties, BoardValue> boardValueCollection;
    public static volatile SingularAttribute<BoardProperties, Users> userID;
    public static volatile SingularAttribute<BoardProperties, Boolean> status;

}