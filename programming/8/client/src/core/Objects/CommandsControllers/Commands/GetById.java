package core.Objects.CommandsControllers.Commands;

import core.Objects.CommandsControllers.Command;
import Commons.CustomPackage;

/** Gets an element by id */
public class GetById extends Command {
    public GetById() {
        super();
    }

    @Override
    public String getName() {
        return "get_by_id";
    }

    @Override
    public String getDescription() {
        return "get element of collection by id";
    }

    @Override
    public Object getRelevantObject() {
       return  null;
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        return pack.getObject().toString() + "\n";
    }

}
