import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IOrganizer, Organizer } from 'app/shared/model/organizer.model';
import { OrganizerService } from './organizer.service';
import { IGame } from 'app/shared/model/game.model';
import { GameService } from 'app/entities/game/game.service';
import { IInstance } from 'app/shared/model/instance.model';
import { InstanceService } from 'app/entities/instance/instance.service';
import { IRuleset } from 'app/shared/model/ruleset.model';
import { RulesetService } from 'app/entities/ruleset/ruleset.service';

@Component({
  selector: 'jhi-organizer-update',
  templateUrl: './organizer-update.component.html'
})
export class OrganizerUpdateComponent implements OnInit {
  isSaving: boolean;

  games: IGame[];

  instances: IInstance[];

  rulesets: IRuleset[];

  editForm = this.fb.group({
    id: [],
    games: [],
    instances: [],
    rulesets: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected organizerService: OrganizerService,
    protected gameService: GameService,
    protected instanceService: InstanceService,
    protected rulesetService: RulesetService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ organizer }) => {
      this.updateForm(organizer);
    });
    this.gameService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IGame[]>) => mayBeOk.ok),
        map((response: HttpResponse<IGame[]>) => response.body)
      )
      .subscribe((res: IGame[]) => (this.games = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.instanceService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IInstance[]>) => mayBeOk.ok),
        map((response: HttpResponse<IInstance[]>) => response.body)
      )
      .subscribe((res: IInstance[]) => (this.instances = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.rulesetService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IRuleset[]>) => mayBeOk.ok),
        map((response: HttpResponse<IRuleset[]>) => response.body)
      )
      .subscribe((res: IRuleset[]) => (this.rulesets = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(organizer: IOrganizer) {
    this.editForm.patchValue({
      id: organizer.id,
      games: organizer.games,
      instances: organizer.instances,
      rulesets: organizer.rulesets
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const organizer = this.createFromForm();
    if (organizer.id !== undefined) {
      this.subscribeToSaveResponse(this.organizerService.update(organizer));
    } else {
      this.subscribeToSaveResponse(this.organizerService.create(organizer));
    }
  }

  private createFromForm(): IOrganizer {
    return {
      ...new Organizer(),
      id: this.editForm.get(['id']).value,
      games: this.editForm.get(['games']).value,
      instances: this.editForm.get(['instances']).value,
      rulesets: this.editForm.get(['rulesets']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrganizer>>) {
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

  trackInstanceById(index: number, item: IInstance) {
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
