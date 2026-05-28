# Instructor Code Review Feedback

**Contact**: Dr. Yiji Zhang (yiji.zhang@northwestern.edu)

**Purpose of This Document**:
The instructor will perform code review with respect to software design, error handling, format and style on the main branch every week starting Week 6 using the letter grade A standards.
The following chapters of the textbook are considered: Chapter 1, 2, 3, 4, 5, 6, 7, 9, and 10. The corresponding lectures are considered, too.

Please note that this feedback does not include evaluation of your progress, the proper use of linters, the quality of your test cases, or your compliance of TDD/BDD workflow.  
You can find the weekly feedback from your dedicated PM/TA for that.

## Week 7-8 Code Review
This review is for the code your team developed in Week 7 and 8.
I apologize for this delayed code review (should have been given last Friday but I got really sick...).
As compensation, I will add one extra code review in Week 10 (around Thursday).

Team, amazing progress made in the past two weeks!! And really good code quality too!!!

The only minor comment I have is for Combovalidator#resolveAction method:

```
			if (card.isType(CardType.SKIP)) { return new SkipAction(); }
			if (card.isType(CardType.ATTACK)) { return new AttackAction(); }
			if (card.isType(CardType.SHUFFLE)) { return new ShuffleAction(); }
			if (card.isType(CardType.SEE_THE_FUTURE)) { return new SeeTheFutureAction(); }
			if (card.isType(CardType.FAVOR)) { return new FavorAction(helper); }
			if (card.isType(CardType.NOPE)) { return new NopeAction(); }
```

could be turned into using if - else if - else if - else (which throws an exception for non-existing enums) to express the mutual exclusivity explicitly.

Good work team!! 


## Week 6 Code Review
There is nothing in the main branch yet so there's nothing for me to review.

Look forward to more code in the next review!

Please approve and merge the PR once the team has read the feedback. Thanks!