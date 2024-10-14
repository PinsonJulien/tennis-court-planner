import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiResponse } from "../../../../dtos/api/api.response.dto";
import { CourtService } from "../../../../services/court.service";
import { CourtDTO } from "../../../../dtos/courts/court.dto";
import { CreateCourtDTO } from "../../../../dtos/courts/create-court.dto";

@Component({
    standalone: true,
    selector: 'app-court-admin-page',
    templateUrl: 'court-admin.page.html',
    styleUrls: ['court-admin.page.scss'],
    imports: [
      CommonModule,
      ReactiveFormsModule,      
    ],
    providers: []
  })
export class CourtAdminPage implements OnInit {
    /**************************************************************************
     * Properties
     * ************************************************************************/

    protected createCourtGroup: FormGroup = new FormGroup({
        name: new FormControl('', [Validators.required]),
        description: new FormControl('', [Validators.required]),
        address: new FormControl('', [Validators.required]),
        imageUrl: new FormControl('', [Validators.required]),
    });

    protected courts: CourtDTO[] = [];

    /**************************************************************************
     * Constructors
     * ************************************************************************/

    constructor(
        protected courtService: CourtService,
    ) {
        //
    }

    /**************************************************************************
     * Methods
     * ************************************************************************/

    ngOnInit(): void {
        this.fetchCourts();
    }

    protected fetchCourts() {
        this.courtService.findAll()
            .subscribe((response: ApiResponse<CourtDTO[]>) => {
                if (response.error) {
                    return;
                }

                this.courts = response.data!;
            });
    }

    protected onCreateSubmit() {
        if (!this.createCourtGroup.valid) {
            return;
        }

        const createCourtDto = new CreateCourtDTO(
            this.createCourtGroup.value.name,
            this.createCourtGroup.value.description,
            this.createCourtGroup.value.address,
            this.createCourtGroup.value.imageUrl
        );

        this.courtService.create(createCourtDto)
            .subscribe((response: ApiResponse<CourtDTO>) => {
                if (response.error) {
                    return;
                }

                const createdCourt: CourtDTO = response.data!;
                this.courts.push(createdCourt);
            });

    }

    protected onUpdateSubmit(id: number) {

    }

    protected onDeleteSubmit(id: number) {
        this.courtService.delete(id)
            .subscribe((response: ApiResponse<void>) => {
                if (response.error) {
                    return;
                }

                this.courts = this.courts.filter(court => court.id !== id);
            });
    }

}