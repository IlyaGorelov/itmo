package core.Objects.Managers;


import Commons.Collection.Product;
import core.Objects.CommandsControllers.Commands.GetById;
import Commons.CustomPackage;
import core.Objects.Connection.Client;

/**
 * Controls id
 */
public class IdManager {
    public static boolean isIdIn(Long id) {
        Product answer = getProductById(id);

        if (answer != null && !AuthManager.getInstance().getUser().equals(answer.getAuthor()))
            throw new IllegalArgumentException("It's not your product");

        return answer != null;
    }

    public static Product getProductById(long id) {
        Client.putCommand(new CustomPackage(
                new GetById().getName(),
                String.valueOf(id),
                null,
                AuthManager.getInstance().getUser()
        ));

        CustomPackage answer = Client.getAnswer();
        if (answer != null) {
            return (Product) (answer.getObject());
        } else {
            return null;
        }
    }

}
