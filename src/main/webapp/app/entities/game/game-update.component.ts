import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IGame, Game } from 'app/shared/model/game.model';
import { GameService } from './game.service';
import { IOrganizer } from 'app/shared/model/organizer.model';
import { OrganizerService } from 'app/entities/organizer/organizer.service';
import { IRuleset } from 'app/shared/model/ruleset.model';
import { RulesetService } from 'app/entities/ruleset/ruleset.service';

@Component({
  selector: 'jhi-game-update',
  templateUrl: './game-update.component.html'
})
export class GameUpdateComponent implements OnInit {
  isSaving: boolean;

  organizers: IOrganizer[];

  rulesets: IRuleset[];

  editForm = this.fb.group({
    id: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected gameService: GameService,
    protected organizerService: OrganizerService,
    protected rulesetService: RulesetService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ game }) => {
      this.updateForm(game);
    });
    this.organizerService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IOrganizer[]>) => mayBeOk.ok),
        map((response: HttpResponse<IOrganizer[]>) => response.body)
      )
      .subscribe((res: IOrganizer[]) => (this.organizers = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.rulesetService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IRuleset[]>) => mayBeOk.ok),
        map((response: HttpResponse<IRuleset[]>) => response.body)
      )
      .subscribe((res: IRuleset[]) => (this.rulesets = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(game: IGame) {
    this.editForm.patchValue({
      id: game.id
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const game = this.createFromForm();
    if (game.id !== undefined) {
      this.subscribeToSaveResponse(this.gameService.update(game));
    } else {
      this.subscribeToSaveResponse(this.gameService.create(game));
    }
  }

  private createFromForm(): IGame {
    return {
      ...new Game(),
      id: this.editForm.get(['id']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGame>>) {
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

  trackOrganizerById(index: number, item: IOrganizer) {
    return item.id;
  }

  trackRulesetById(index: number, item: IRuleset) {
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
