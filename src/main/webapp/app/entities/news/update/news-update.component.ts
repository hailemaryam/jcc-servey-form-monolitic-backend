import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { INews, News } from '../news.model';
import { NewsService } from '../service/news.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-news-update',
  templateUrl: './news-update.component.html',
})
export class NewsUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    title: [],
    detail: [],
    createdBy: [],
    registeredTime: [],
    updateTime: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected newsService: NewsService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ news }) => {
      if (news.id === undefined) {
        const today = dayjs().startOf('day');
        news.registeredTime = today;
        news.updateTime = today;
      }

      this.updateForm(news);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('rmtMonoliticApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const news = this.createFromForm();
    if (news.id !== undefined) {
      this.subscribeToSaveResponse(this.newsService.update(news));
    } else {
      this.subscribeToSaveResponse(this.newsService.create(news));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INews>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(news: INews): void {
    this.editForm.patchValue({
      id: news.id,
      title: news.title,
      detail: news.detail,
      createdBy: news.createdBy,
      registeredTime: news.registeredTime ? news.registeredTime.format(DATE_TIME_FORMAT) : null,
      updateTime: news.updateTime ? news.updateTime.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): INews {
    return {
      ...new News(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      detail: this.editForm.get(['detail'])!.value,
      createdBy: this.editForm.get(['createdBy'])!.value,
      registeredTime: this.editForm.get(['registeredTime'])!.value
        ? dayjs(this.editForm.get(['registeredTime'])!.value, DATE_TIME_FORMAT)
        : undefined,
      updateTime: this.editForm.get(['updateTime'])!.value ? dayjs(this.editForm.get(['updateTime'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
