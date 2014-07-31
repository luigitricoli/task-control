package br.com.egs.task.control.web.interceptor;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.egs.task.control.web.controller.AuthController;
import br.com.egs.task.control.web.model.SessionUser;

import javax.inject.Inject;

@Intercepts
public class AuthInterceptor implements Interceptor {

    @Inject
    private SessionUser user;
    @Inject
    private Result result;

    @Override
    public void intercept(InterceptorStack stack, ControllerMethod method, Object obj) throws InterceptionException {
        if (user.isLogged()) {
            stack.next(method, obj);
        } else {
            result.redirectTo(AuthController.class).index();
        }
    }

    @Override
    public boolean accepts(ControllerMethod method) {
        return method.getMethod().isAnnotationPresent(AuthRequired.class)
                || method.getController().getType().isAnnotationPresent(AuthRequired.class);
    }
}
