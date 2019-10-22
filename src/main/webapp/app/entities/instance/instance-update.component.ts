import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IInstance, Instance } from 'app/shared/model/instance.model';
import { InstanceService } from './instance.service';
import { IGame } from 'app/shared/model/game.model';
import { GameService } from 'app/entities/game/game.service';
import { IOrganizer } from 'app/shared/model/organizer.model';
import { OrganizerService } from 'app/entities/organizer/organizer.service';

@Component({
  selector: 'jhi-instance-update',
  templateUrl: './instance-update.component.html'
})
export class InstanceUpdateComponent implements OnInit {
  isSaving: boolean;

  games: IGame[];

  organizers: IOrganizer[];

  editForm = this.fb.group({
    id: [],
    game: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected instanceService: InstanceService,
    protected gameService: GameService,
    protected organizerService: OrganizerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ instance }) => {
      this.updateForm(instance);
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

  updateForm(instance: IInstance) {
    this.editForm.patchValue({
      id: instance.id,
      game: instance.game
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const instance = this.createFromForm();
    if (instance.id !== undefined) {
      this.subscribeToSaveResponse(this.instanceService.update(instance));
    } else {
      this.subscribeToSaveResponse(this.instanceService.create(instance));
    }
  }

  private createFromForm(): IInstance {
    return {
      ...new Instance(),
      id: this.editForm.get(['id']).value,
      game: this.editForm.get(['game']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInstance>>) {
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
