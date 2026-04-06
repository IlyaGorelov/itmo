package Objects.CommandsControllers.Commands;

import Objects.CommandsControllers.Command;
import Objects.Connection.CustomPackage;
import Objects.Validators.*;

/** Gets an element by id */
public class GetById extends Command {
    public GetById(boolean hasArgument) {
        super(hasArgument, false);
    }

    public GetById() {
        super();
    }

    @Override
    public String getName() {
        return "get_by_id";
    }

    @Override
    public Object getRelevantObject() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getRelevantObject'");
    }

    @Override
    public String getRelevantAnswer(CustomPackage pack) {
        return pack.getObject().toString() + "\n";
    }

}
