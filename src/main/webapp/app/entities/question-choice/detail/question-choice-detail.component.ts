import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IQuestionChoice } from '../question-choice.model';

@Component({
  selector: 'jhi-question-choice-detail',
  templateUrl: './question-choice-detail.component.html',
})
export class QuestionChoiceDetailComponent implements OnInit {
  questionChoice: IQuestionChoice | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ questionChoice }) => {
      this.questionChoice = questionChoice;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
