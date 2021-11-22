import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IAnswer, Answer } from '../answer.model';
import { AnswerService } from '../service/answer.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IQuestion } from 'app/entities/question/question.model';
import { QuestionService } from 'app/entities/question/service/question.service';
import { IFormProgresss } from 'app/entities/form-progresss/form-progresss.model';
import { FormProgresssService } from 'app/entities/form-progresss/service/form-progresss.service';
import { DataType } from 'app/entities/enumerations/data-type.model';

@Component({
  selector: 'jhi-answer-update',
  templateUrl: './answer-update.component.html',
})
export class AnswerUpdateComponent implements OnInit {
  isSaving = false;
  dataTypeValues = Object.keys(DataType);

  usersSharedCollection: IUser[] = [];
  questionsSharedCollection: IQuestion[] = [];
  formProgresssesSharedCollection: IFormProgresss[] = [];

  editForm = this.fb.group({
    id: [],
    number: [],
    booleanAnswer: [],
    shortAnswer: [],
    paragraph: [],
    multipleChoice: [],
    dropDown: [],
    fileUploaded: [],
    fileUploadedContentType: [],
    fileName: [],
    date: [],
    time: [],
    submitedOn: [],
    dataType: [],
    user: [],
    question: [],
    formProgresss: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected answerService: AnswerService,
    protected userService: UserService,
    protected questionService: QuestionService,
    protected formProgresssService: FormProgresssService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ answer }) => {
      if (answer.id === undefined) {
        const today = dayjs().startOf('day');
        answer.date = today;
        answer.submitedOn = today;
      }

      this.updateForm(answer);

      this.loadRelationshipsOptions();
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
    const answer = this.createFromForm();
    if (answer.id !== undefined) {
      this.subscribeToSaveResponse(this.answerService.update(answer));
    } else {
      this.subscribeToSaveResponse(this.answerService.create(answer));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackQuestionById(index: number, item: IQuestion): number {
    return item.id!;
  }

  trackFormProgresssById(index: number, item: IFormProgresss): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAnswer>>): void {
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

  protected updateForm(answer: IAnswer): void {
    this.editForm.patchValue({
      id: answer.id,
      number: answer.number,
      booleanAnswer: answer.booleanAnswer,
      shortAnswer: answer.shortAnswer,
      paragraph: answer.paragraph,
      multipleChoice: answer.multipleChoice,
      dropDown: answer.dropDown,
      fileUploaded: answer.fileUploaded,
      fileUploadedContentType: answer.fileUploadedContentType,
      fileName: answer.fileName,
      date: answer.date ? answer.date.format(DATE_TIME_FORMAT) : null,
      time: answer.time,
      submitedOn: answer.submitedOn ? answer.submitedOn.format(DATE_TIME_FORMAT) : null,
      dataType: answer.dataType,
      user: answer.user,
      question: answer.question,
      formProgresss: answer.formProgresss,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, answer.user);
    this.questionsSharedCollection = this.questionService.addQuestionToCollectionIfMissing(this.questionsSharedCollection, answer.question);
    this.formProgresssesSharedCollection = this.formProgresssService.addFormProgresssToCollectionIfMissing(
      this.formProgresssesSharedCollection,
      answer.formProgresss
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.questionService
      .query()
      .pipe(map((res: HttpResponse<IQuestion[]>) => res.body ?? []))
      .pipe(
        map((questions: IQuestion[]) =>
          this.questionService.addQuestionToCollectionIfMissing(questions, this.editForm.get('question')!.value)
        )
      )
      .subscribe((questions: IQuestion[]) => (this.questionsSharedCollection = questions));

    this.formProgresssService
      .query()
      .pipe(map((res: HttpResponse<IFormProgresss[]>) => res.body ?? []))
      .pipe(
        map((formProgressses: IFormProgresss[]) =>
          this.formProgresssService.addFormProgresssToCollectionIfMissing(formProgressses, this.editForm.get('formProgresss')!.value)
        )
      )
      .subscribe((formProgressses: IFormProgresss[]) => (this.formProgresssesSharedCollection = formProgressses));
  }

  protected createFromForm(): IAnswer {
    return {
      ...new Answer(),
      id: this.editForm.get(['id'])!.value,
      number: this.editForm.get(['number'])!.value,
      booleanAnswer: this.editForm.get(['booleanAnswer'])!.value,
      shortAnswer: this.editForm.get(['shortAnswer'])!.value,
      paragraph: this.editForm.get(['paragraph'])!.value,
      multipleChoice: this.editForm.get(['multipleChoice'])!.value,
      dropDown: this.editForm.get(['dropDown'])!.value,
      fileUploadedContentType: this.editForm.get(['fileUploadedContentType'])!.value,
      fileUploaded: this.editForm.get(['fileUploaded'])!.value,
      fileName: this.editForm.get(['fileName'])!.value,
      date: this.editForm.get(['date'])!.value ? dayjs(this.editForm.get(['date'])!.value, DATE_TIME_FORMAT) : undefined,
      time: this.editForm.get(['time'])!.value,
      submitedOn: this.editForm.get(['submitedOn'])!.value ? dayjs(this.editForm.get(['submitedOn'])!.value, DATE_TIME_FORMAT) : undefined,
      dataType: this.editForm.get(['dataType'])!.value,
      user: this.editForm.get(['user'])!.value,
      question: this.editForm.get(['question'])!.value,
      formProgresss: this.editForm.get(['formProgresss'])!.value,
    };
  }
}
