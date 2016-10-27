package com.yetthin.web.persistence;

import java.util.List;

import com.yetthin.web.domain.HeroIncome;
import com.yetthin.web.domain.RecommendList;

public interface IndexPageContextMapper {
	List<RecommendList> getIncomeRecommendList(int type,int begin,int end);
	List<HeroIncome> getBestIncomeList();
	int  getTotlePageIncome(int type);
	int  getTotlePageHero();
}
