@import hexlet.code.util.NamedRoutes
@import hexlet.code.dto.BasePage
@import org.apache.commons.lang3.StringUtils
@param BasePage page

@template.layout.page(
    content = @`
        <svg xmlns="http://www.w3.org/2000/svg" class="d-none">
            <symbol id="exclamation-triangle-fill" viewBox="0 0 16 16">
                <path d="M8.982 1.566a1.13 1.13 0 0 0-1.96 0L.165 13.233c-.457.778.091 1.767.98 1.767h13.713c.889 0 1.438-.99.98-1.767L8.982 1.566zM8 5c.535 0 .954.462.9.995l-.35 3.507a.552.552 0 0 1-1.1 0L7.1 5.995A.905.905 0 0 1 8 5zm.002 6a1 1 0 1 1 0 2 1 1 0 0 1 0-2z"/>
            </symbol>
        </svg>
        @if(StringUtils.contains(page.getMessage(), "Некорректный"))
            <div class="alert alert-danger d-flex align-items-center alert-dismissible fade show m-1" role="alert">
                <svg class="bi flex-shrink-0 me-2" role="img" aria-label="Danger:"><use xlink:href="#exclamation-triangle-fill"/></svg>
                <div>
                    ${page.getMessage()}
                </div>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        @endif
        <div class="text-bg-dark">
            <div class="d-flex flex-column pb-1 col-md-10 col-lg-8 mx-auto text-white">
                <h1 class="display-3 fw-bolder mb-0">Анализатор страниц</h1>
                <p class="fs-4 fw-lighter">Бесплатно проверяйте сайты на SEO пригодность</p>
            </div>
            <div class="my-1">
                <form class="row-auto" action="${NamedRoutes.rootPath()}" method="post">
                    <div class="col">
                        <div class="form-floating">
                            <input type="text" class="form-control" id="url-input" placeholder="Ссылка" name="url">
                            <label for="url-input">Ссылка</label>
                        </div>
                        <div>
                            <button type="submit" class="btn btn-primary mb-3">Проверить</button>
                        </div>
                    </div>
                </form>
            </div>
            <div class="text-muted fst-italic">
                Пример: https://www.example.com
            </div>
        </div>
    `
)