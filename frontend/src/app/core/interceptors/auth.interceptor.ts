import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // Clone outgoing request to include credentials (cookies) automatically
  const clonedReq = req.clone({
    withCredentials: true
  });

  return next(clonedReq);
};
