package br.com.egs.task.control.web.interceptor;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.caelum.vraptor.resource.ResourceMethod;
import br.com.egs.task.control.web.controller.AuthController;
import br.com.egs.task.control.web.model.User;

@Intercepts
public class AuthInterceptor implements Interceptor {

    private User user;
    private Result result;

    public AuthInterceptor(User user, Result result) {
        this.user = user;
        this.result = result;
    }

    @Override
    public void intercept(InterceptorStack stack, ResourceMethod method, Object obj) throws InterceptionException {
        if (user.isLogged()) {
            stack.next(method, obj);
        } else {
            result.redirectTo(AuthController.class).index();
        }
    }

    @Override
    public boolean accepts(ResourceMethod method) {
        return method.getMethod().isAnnotationPresent(AuthRequired.class)
                || method.getResource().getType().isAnnotationPresent(AuthRequired.class);
    }
}
