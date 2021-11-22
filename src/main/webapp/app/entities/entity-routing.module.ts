import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'form',
        data: { pageTitle: 'rmtMonoliticApp.form.home.title' },
        loadChildren: () => import('./form/form.module').then(m => m.FormModule),
      },
      {
        path: 'question',
        data: { pageTitle: 'rmtMonoliticApp.question.home.title' },
        loadChildren: () => import('./question/question.module').then(m => m.QuestionModule),
      },
      {
        path: 'question-choice',
        data: { pageTitle: 'rmtMonoliticApp.questionChoice.home.title' },
        loadChildren: () => import('./question-choice/question-choice.module').then(m => m.QuestionChoiceModule),
      },
      {
        path: 'form-progresss',
        data: { pageTitle: 'rmtMonoliticApp.formProgresss.home.title' },
        loadChildren: () => import('./form-progresss/form-progresss.module').then(m => m.FormProgresssModule),
      },
      {
        path: 'answer',
        data: { pageTitle: 'rmtMonoliticApp.answer.home.title' },
        loadChildren: () => import('./answer/answer.module').then(m => m.AnswerModule),
      },
      {
        path: 'multiple-choice-ansewer',
        data: { pageTitle: 'rmtMonoliticApp.multipleChoiceAnsewer.home.title' },
        loadChildren: () => import('./multiple-choice-ansewer/multiple-choice-ansewer.module').then(m => m.MultipleChoiceAnsewerModule),
      },
      {
        path: 'type-of-organization',
        data: { pageTitle: 'rmtMonoliticApp.typeOfOrganization.home.title' },
        loadChildren: () => import('./type-of-organization/type-of-organization.module').then(m => m.TypeOfOrganizationModule),
      },
      {
        path: 'company',
        data: { pageTitle: 'rmtMonoliticApp.company.home.title' },
        loadChildren: () => import('./company/company.module').then(m => m.CompanyModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
