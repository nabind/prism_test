package com.ctb.prism.test;

import java.util.ArrayList;
import java.util.List;

import com.ctb.prism.parent.transferobject.QuestionTO;

public class ParentTestHelper {

	public static List<QuestionTO> getQuestionList() {
		List<QuestionTO> questionToList = new ArrayList<QuestionTO>();
		QuestionTO question = new QuestionTO();
		question.setQuestionId(1L);
		question.setAnswerId(1L);
		question.setAnswer("1");
		questionToList.add(question);
		question = new QuestionTO();
		question.setQuestionId(2L);
		question.setAnswerId(2L);
		question.setAnswer("2");
		questionToList.add(question);
		question = new QuestionTO();
		question.setQuestionId(3L);
		question.setAnswerId(3L);
		question.setAnswer("3");
		questionToList.add(question);
		return questionToList;
	}
}
