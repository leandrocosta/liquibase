package liquibase.change.core

import liquibase.change.ChangeStatus;
import liquibase.change.StandardChangeTest;
import liquibase.sdk.database.MockDatabase
import liquibase.snapshot.MockSnapshotFactory
import liquibase.snapshot.SnapshotFactory
import liquibase.structure.core.Table

public class DropTableChangeTest extends StandardChangeTest {
    def getConfirmationMessage() throws Exception {
        when:
        def change = new DropTableChange();
        change.setSchemaName("SCHEMA_NAME");
        change.setTableName("TAB_NAME");
        change.setCascadeConstraints(true);

        then:
        "Table TAB_NAME dropped" == change.getConfirmationMessage()
    }

    def "checkStatus"() {
        when:
        def database = new MockDatabase()
        def snapshotFactory = new MockSnapshotFactory()
        SnapshotFactory.instance = snapshotFactory

        def table = new Table(null, null, "test_table")

        def change = new DropTableChange()
        change.tableName = table.name

        then: "table is not there yet"
        assert change.checkStatus(database).status == ChangeStatus.Status.complete

        when: "Table exists"
        snapshotFactory.addObjects(table)
        then:
        assert change.checkStatus(database).status == ChangeStatus.Status.notApplied
   }
}