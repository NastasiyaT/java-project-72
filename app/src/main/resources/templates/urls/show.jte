@import hexlet.code.dto.urls.UrlPage
@import hexlet.code.util.NamedRoutes
@import java.text.SimpleDateFormat
@param UrlPage page

@template.layout.page(
    content = @`
        <div class="mx-4 mt-4 mb-0">
            <div class="flex-row fs-6">
                <table class="table table-light table-hover table-bordered border-white border-4 caption-top table-responsive">
                    <caption class="fs-4">Сайт <span class="fw-light fst-italic">${page.getUrl().getName()}</span></caption>
                    <tbody class="m-2">
                    <tr>
                        <th scope="row">ID</th>
                        <td>${page.getUrl().getId()}</td>
                    </tr>
                    <tr>
                        <th scope="row">Название</th>
                        <td>${page.getUrl().getName()}</td>
                    </tr>
                    <tr>
                        <th scope="row">Дата добавления</th>
                        <td>${new SimpleDateFormat("dd/MM/yy HH:mm").format(page.getUrl().getCreatedAt())}</td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <div class="d-flex py-3">
                <form action="${NamedRoutes.urlCheckPath(page.getUrl().getId())}" method="post" class="rss-form">
                    <button type="submit" class="h-100 btn btn-lg btn-success opacity-75 px-sm-5">Запустить проверку</button>
                </form>
            </div>

            !{var checks = page.getChecks();}
            @if(!checks.isEmpty())
                <div class="flex-row fs-6 mt-2">
                    <table class="table table-light table-hover table-bordered border-white border-4 caption-top">
                        <caption class="fs-4">Проверки</caption>
                        <thead>
                        <tr>
                            <th class="col-1" scope="col">ID</th>
                            <th class="col-1" scope="col">Код ответа</th>
                            <th class="col-2" scope="col">h1</th>
                            <th class="col-2" scope="col">title</th>
                            <th class="col-auto" scope="col">description</th>
                            <th class="col-2" scope="col">Дата проверки</th>
                        </tr>
                        </thead>
                        <tbody>
                        @for(int i = checks.size() - 1; i >= 0; i--)
                            !{var check = page.getChecks().get(i);}
                            <tr>
                                <td>${check.getId()}</td>
                                <td>${check.getStatusCode()}</td>
                                <td>${check.getH1()}</td>
                                <td>${check.getTitle()}</td>
                                <td>${check.getDescription()}</td>
                                <td>${new SimpleDateFormat("dd/MM/yy HH:mm").format(check.getCreatedAt())}</td>
                            </tr>
                        @endfor
                        </tbody>
                    </table>
                </div>
            @endif
        </div>
    `
)
