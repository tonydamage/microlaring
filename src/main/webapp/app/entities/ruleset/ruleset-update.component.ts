import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IRuleset, Ruleset } from 'app/shared/model/ruleset.model';
import { RulesetService } from './ruleset.service';
import { IGame } from 'app/shared/model/game.model';
import { GameService } from 'app/entities/game/game.service';
import { IOrganizer } from 'app/shared/model/organizer.model';
import { OrganizerService } from 'app/entities/organizer/organizer.service';

@Component({
  selector: 'jhi-ruleset-update',
  templateUrl: './ruleset-update.component.html'
})
export class RulesetUpdateComponent implements OnInit {
  isSaving: boolean;

  games: IGame[];

  organizers: IOrganizer[];

  editForm = this.fb.group({
    id: [],
    games: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected rulesetService: RulesetService,
    protected gameService: GameService,
    protected organizerService: OrganizerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ ruleset }) => {
      this.updateForm(ruleset);
    });
    this.gameService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IGame[]>) => mayBeOk.ok),
        map((response: HttpResponse<IGame[]>) => response.body)
      )
      .subscribe((res: IGame[]) => (this.games = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.organizerService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IOrganizer[]>) => mayBeOk.ok),
        map((response: HttpResponse<IOrganizer[]>) => response.body)
      )
      .subscribe((res: IOrganizer[]) => (this.organizers = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(ruleset: IRuleset) {
    this.editForm.patchValue({
      id: ruleset.id,
      games: ruleset.games
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const ruleset = this.createFromForm();
    if (ruleset.id !== undefined) {
      this.subscribeToSaveResponse(this.rulesetService.update(ruleset));
    } else {
      this.subscribeToSaveResponse(this.rulesetService.create(ruleset));
    }
  }

  private createFromForm(): IRuleset {
    return {
      ...new Ruleset(),
      id: this.editForm.get(['id']).value,
      games: this.editForm.get(['games']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRuleset>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackGameById(index: number, item: IGame) {
    return item.id;
  }

  trackOrganizerById(index: number, item: IOrganizer) {
    return item.id;
  }

  getSelected(selectedVals: any[], option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
