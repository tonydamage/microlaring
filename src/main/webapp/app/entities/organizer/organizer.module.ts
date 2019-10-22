import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MicrolarpingSharedModule } from 'app/shared/shared.module';
import { OrganizerComponent } from './organizer.component';
import { OrganizerDetailComponent } from './organizer-detail.component';
import { OrganizerUpdateComponent } from './organizer-update.component';
import { OrganizerDeletePopupComponent, OrganizerDeleteDialogComponent } from './organizer-delete-dialog.component';
import { organizerRoute, organizerPopupRoute } from './organizer.route';

const ENTITY_STATES = [...organizerRoute, ...organizerPopupRoute];

@NgModule({
  imports: [MicrolarpingSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    OrganizerComponent,
    OrganizerDetailComponent,
    OrganizerUpdateComponent,
    OrganizerDeleteDialogComponent,
    OrganizerDeletePopupComponent
  ],
  entryComponents: [OrganizerDeleteDialogComponent]
})
export class MicrolarpingOrganizerModule {}
