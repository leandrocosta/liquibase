package liquibase.change.core;

import liquibase.change.*;
import  liquibase.ExecutionEnvironment;
import liquibase.snapshot.SnapshotFactory;
import liquibase.statement.Statement;
import liquibase.statement.core.DropViewStatement;
import liquibase.structure.core.View;

/**
 * Drops an existing view.
 */
@DatabaseChange(name="dropView", description = "Drops an existing view", priority = ChangeMetaData.PRIORITY_DEFAULT, appliesTo = "view")
public class DropViewChange extends AbstractChange {
    private String catalogName;
    private String schemaName;
    private String viewName;


    @DatabaseChangeProperty(mustEqualExisting ="view.catalog", since = "3.0")
    public String getCatalogName() {
        return catalogName;
    }

    public void setCatalogName(String catalogName) {
        this.catalogName = catalogName;
    }

    @DatabaseChangeProperty(mustEqualExisting ="view.schema")
    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    @DatabaseChangeProperty(mustEqualExisting = "view", description = "Name of the view to drop")
    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    @Override
    public Statement[] generateStatements(ExecutionEnvironment env) {
        return new Statement[]{
                new DropViewStatement(getCatalogName(), getSchemaName(), getViewName()),
        };
    }

    @Override
    public ChangeStatus checkStatus(ExecutionEnvironment env) {
        try {
            return new ChangeStatus().assertComplete(!SnapshotFactory.getInstance().has(new View(getCatalogName(), getSchemaName(), getViewName()), env.getTargetDatabase()), "View exists");
        } catch (Exception e) {
            return new ChangeStatus().unknown(e);
        }
    }


    @Override
    public String getConfirmationMessage() {
        return "View "+getViewName()+" dropped";
    }

    @Override
    public String getSerializedObjectNamespace() {
        return STANDARD_CHANGELOG_NAMESPACE;
    }
}
