import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFormProgresss } from '../form-progresss.model';

@Component({
  selector: 'jhi-form-progresss-detail',
  templateUrl: './form-progresss-detail.component.html',
})
export class FormProgresssDetailComponent implements OnInit {
  formProgresss: IFormProgresss | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formProgresss }) => {
      this.formProgresss = formProgresss;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
