import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMultipleChoiceAnsewer } from '../multiple-choice-ansewer.model';

@Component({
  selector: 'jhi-multiple-choice-ansewer-detail',
  templateUrl: './multiple-choice-ansewer-detail.component.html',
})
export class MultipleChoiceAnsewerDetailComponent implements OnInit {
  multipleChoiceAnsewer: IMultipleChoiceAnsewer | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ multipleChoiceAnsewer }) => {
      this.multipleChoiceAnsewer = multipleChoiceAnsewer;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
