
export class ApiResponse<T> {
    apiVersion: string = "";
    context: string = "";
    id: string = "";
    method: string = "";
    params: Object = {};
    data: T | null = null;
    error: ApiErrorDTO | null = null;

    public constructor(data: Partial<ApiResponse<T>>) {
        Object.assign(this, data);
    }
}

export class ApiErrorDTO {
    code: number = 0;
    message: string = "";
    errors: ErrorDTO[] = [];    

    public constructor(data: Partial<ApiErrorDTO>) {
        Object.assign(this, data);
    }
}

export class ErrorDTO {
    domain: string = "";
    reason: string = "";
    message: string = "";
    location: string = "";
    locationType: string = "";
    extendedHelp: string = "";
    sendReport: string = "";

    public constructor(data: Partial<ErrorDTO>) {
        Object.assign(this, data);
    }
}



