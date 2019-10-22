import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MicrolarpingSharedModule } from 'app/shared/shared.module';
import { RulesetComponent } from './ruleset.component';
import { RulesetDetailComponent } from './ruleset-detail.component';
import { RulesetUpdateComponent } from './ruleset-update.component';
import { RulesetDeletePopupComponent, RulesetDeleteDialogComponent } from './ruleset-delete-dialog.component';
import { rulesetRoute, rulesetPopupRoute } from './ruleset.route';

const ENTITY_STATES = [...rulesetRoute, ...rulesetPopupRoute];

@NgModule({
  imports: [MicrolarpingSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    RulesetComponent,
    RulesetDetailComponent,
    RulesetUpdateComponent,
    RulesetDeleteDialogComponent,
    RulesetDeletePopupComponent
  ],
  entryComponents: [RulesetDeleteDialogComponent]
})
export class MicrolarpingRulesetModule {}
