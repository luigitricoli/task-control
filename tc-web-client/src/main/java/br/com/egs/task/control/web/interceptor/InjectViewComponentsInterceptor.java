package br.com.egs.task.control.web.interceptor;

import br.com.caelum.vraptor.InterceptionException;
import br.com.caelum.vraptor.Intercepts;
import br.com.caelum.vraptor.Result;
import br.com.caelum.vraptor.controller.ControllerMethod;
import br.com.caelum.vraptor.core.InterceptorStack;
import br.com.caelum.vraptor.interceptor.Interceptor;
import br.com.egs.task.control.web.controller.AuthController;
import br.com.egs.task.control.web.model.SessionUser;
import br.com.egs.task.control.web.model.filter.Applications;
import br.com.egs.task.control.web.model.filter.Sources;

import javax.inject.Inject;

@Intercepts
public class InjectViewComponentsInterceptor implements Interceptor {

    @Inject
    private SessionUser user;
    @Inject
    private Applications applications;
    @Inject
    private Sources sources;
    @Inject
    private Result result;

    @Override
    public void intercept(InterceptorStack stack, ControllerMethod method, Object obj) throws InterceptionException {
        stack.next(method, obj);
        result.include("sources", sources);
        result.include("applications", applications);
        result.include("sessionUser", user);
    }

    @Override
    public boolean accepts(ControllerMethod method) {
        return true;
    }
}
