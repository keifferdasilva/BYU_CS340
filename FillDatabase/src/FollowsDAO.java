import java.util.List;

public interface FollowsDAO {


    void addFollowersBatch(List<String> followers, String followTarget);
}
