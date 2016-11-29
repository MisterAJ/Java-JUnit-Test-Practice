package com.teamtreehouse.techdegree.overboard.model;

import com.teamtreehouse.techdegree.overboard.exc.AnswerAcceptanceException;
import com.teamtreehouse.techdegree.overboard.exc.VotingException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class UserTest {
    private User mUser;
    private User mUserTwo;
    private Question mQuestion;
    private Answer mAnswer;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        Board board = new Board("test");
        mUser = new User(board,"testUser");
        mUserTwo = new User(board,"userTwo");
        mQuestion = mUser.askQuestion("testQuestion");
        mAnswer = mUserTwo.answerQuestion(mQuestion,"testAnswer");
    }

    @Test
    public void upVoteQuestionIncreasesReputationByFive() {
        mUserTwo.upVote(mQuestion);
        assertEquals(5, mUser.getReputation());
    }

    @Test
    public void upVoteAnswerIncreasesReputationByTen() {
        mAnswer.addUpVoter(mUser);
        assertEquals(10, mUserTwo.getReputation());
    }

    @Test
    public void answerAcceptedGivesFifteenReputation(){
        int rep = mUserTwo.getReputation();
        rep += 15;
        mUser.acceptAnswer(mAnswer);
        assertEquals(rep,mUserTwo.getReputation());
    }

    @Test (expected = VotingException.class)
    public void cannotVoteUpOwnQuestion(){
        mUser.upVote(mQuestion);
    }

    @Test (expected = VotingException.class)
    public void cannotVoteDownOwnQuestion(){
        mUser.downVote(mQuestion);
    }

    @Test (expected = VotingException.class)
    public void cannotVoteUpOwnAnswer(){
        mUserTwo.upVote(mAnswer);
    }

    @Test (expected = VotingException.class)
    public void cannotVoteDownOwnAnswer(){
        mUserTwo.downVote(mAnswer);
    }

    @Test
    public void onlyQuestionerCanAcceptAnswer() throws Exception {
        String message = String.format("Only %s can accept this answer as it is their question",
                                        mUser.getName());
        thrown.expect(AnswerAcceptanceException.class);
        thrown.expectMessage(message);

        mUserTwo.acceptAnswer(mAnswer);
    }

    @Test
    public void downVoteOfQuestionDoesNotAffectReputation(){
        int rep = mUser.getReputation();
        mUserTwo.downVote(mQuestion);
        assertEquals(rep, mUser.getReputation());
    }

    @Test
    public void downVoteOfAnswerLosesOneReputation(){
        int rep = (mUserTwo.getReputation() - 1);
        mUser.downVote(mAnswer);
        assertEquals(rep, mUserTwo.getReputation());
    }
}