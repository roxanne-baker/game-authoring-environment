package voogasalad.util.facebookutil.user.profiles;

import voogasalad.util.facebookutil.scores.HighScoreBoard;
import voogasalad.util.facebookutil.scores.ScoreOrder;
import voogasalad.util.facebookutil.user.IUser;

/**
 * User profile for a twitter user
 * @author Tommy
 *
 */
public class TwitterProfile extends UserProfile{

    public TwitterProfile (String userID) {
        super(userID);
    }

    @Override
    public void customPost (String message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void highScoreBoardPost (HighScoreBoard board, String gameName, ScoreOrder order) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void challenge (IUser source, IUser target, String message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void highScorePost (HighScoreBoard board,
                               String gameName,
                               IUser user,
                               ScoreOrder order) {
        // TODO Auto-generated method stub
        
    }

}