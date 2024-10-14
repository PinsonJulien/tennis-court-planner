import { CommonModule } from "@angular/common";
import { Component, OnInit } from "@angular/core";
import { ApiErrorDTO, ApiResponse } from "../../../../dtos/api/api.response.dto";
import { CourtService } from "../../../../services/court.service";
import { CourtDTO } from "../../../../dtos/courts/court.dto";
import { CreateCourtDTO } from "../../../../dtos/courts/create-court.dto";
import { NotificationService } from "../../../../services/notification.service";
import { MatCardModule } from "@angular/material/card";
import { MatFormFieldModule } from "@angular/material/form-field";
import { MatInputModule } from "@angular/material/input";
import { MatButtonModule } from "@angular/material/button";
import { MatTableModule } from "@angular/material/table";
import { MatIconModule } from "@angular/material/icon";
import { MatDialog, MatDialogModule } from "@angular/material/dialog";
import { CourtDialogComponent } from "./dialogs/court-dialog.component";
import { PartialUpdateCourtDTO } from "../../../../dtos/courts/partial-update-court.dto";

@Component({
    standalone: true,
    selector: 'app-court-admin-page',
    templateUrl: 'court-admin.page.html',
    styleUrls: ['court-admin.page.scss'],
    imports: [
      CommonModule,
      MatCardModule,
      MatFormFieldModule,
      MatInputModule,
      MatButtonModule,
      MatTableModule,
      MatIconModule,    
      MatDialogModule,  
    ],
    providers: []
  })
export class CourtAdminPage implements OnInit {
    /**************************************************************************
     * Properties
     * ************************************************************************/

    protected courts: CourtDTO[] = [];
    private editedCourt: CourtDTO | null = null;

    /**************************************************************************
     * Constructors
     * ************************************************************************/

    constructor(
        protected courtService: CourtService,
        private notificationService: NotificationService,
        private dialog: MatDialog,
    ) {
        //
    }

    /**************************************************************************
     * Methods
     * ************************************************************************/

    ngOnInit(): void {
        this.refresh();
    }

    protected refresh(): void {
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

    protected onCreateSubmit(data: any): void {
        const createCourtDto = new CreateCourtDTO(
            data.name,
            data.description,
            data.address,
            data.imageUrl
        );

        this.courtService.create(createCourtDto)
            .subscribe((response: ApiResponse<CourtDTO>) => {
                if (response.error) {
                    this.showErrorMessage(response.error);
                    return;
                }

                const createdCourt: CourtDTO = response.data!;
                this.showSuccessMessage('Court created successfully');
                this.refresh();
            });
    }

    protected onUpdateSubmit(data: any) {
        if (!this.editedCourt) {
            return;
        }
        const id = this.editedCourt.id;
        const updateCourtDto: PartialUpdateCourtDTO = {}
        if (data.name && data.name !== this.editedCourt.name) {
            updateCourtDto.name = data.name;
        }
        if (data.description && data.description !== this.editedCourt.description) {
            updateCourtDto.description = data.description;
        }
        if (data.address && data.address !== this.editedCourt.address) {
            updateCourtDto.address = data.address;
        }
        if (data.imageUrl && data.imageUrl !== this.editedCourt.imageUrl) {
            updateCourtDto.imageUrl = data.imageUrl;
        }

        this.courtService.edit(id, updateCourtDto)
            .subscribe((response: ApiResponse<CourtDTO>) => {
                if (response.error) {
                    this.showErrorMessage(response.error);
                    return;
                }

                this.showSuccessMessage('Court updated successfully');
                this.refresh();
            });
    }

    protected onDeleteSubmit(id: number) {
        this.courtService.delete(id)
            .subscribe((response: ApiResponse<void>) => {
                if (response.error) {
                    this.showErrorMessage(response.error);
                    return;
                }

                this.showSuccessMessage('Court deleted successfully');
                this.refresh();
            });
    }

    protected showErrorMessage(error: ApiErrorDTO): void {
        this.notificationService.showErrorMessage(error.message);
    }

    protected showSuccessMessage(message: string): void {
        this.notificationService.showSuccessMessage(message);
    }

    protected openCreateCourtDialog(): void {
        const dialogRef = this.dialog.open(CourtDialogComponent, {
            width: '400px',
            data: null, // Pass null for creating a new court
        });
    
        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.onCreateSubmit(result);
            }
        });
    }

    protected openEditCourtDialog(court: CourtDTO): void {
        this.editedCourt = court;
        const dialogRef = this.dialog.open(CourtDialogComponent, {
            width: '400px',
            data: court, // Pass court data for editing
        });

        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.onUpdateSubmit(result);
            }
        });
    }

}