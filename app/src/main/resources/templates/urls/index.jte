@import hexlet.code.util.NamedRoutes
@import hexlet.code.model.Url
@import hexlet.code.dto.urls.UrlsPage
@param UrlsPage page

@template.layout.page(
content = @`
    !{var message = page.getMessage();}
    @if(message != null)
        @if(message.contains("успешно"))
            <div class="alert alert-success shadow-sm p-3 mb-5 bg-body rounded" role="alert">
                ${message}
                <button type="button" class="btn-close position-absolute top-50 start-100 translate-middle" aria-label="Close"></button>
            </div>
        @elseif(message.contains("уже"))
            <div class="alert alert-warning shadow-sm p-3 mb-5 bg-body rounded" role="alert">
                ${message}
                <button type="button" class="btn-close position-absolute top-50 start-100 translate-middle" aria-label="Close"></button>
            </div>
        @endif
    @endif

    !{var pageCount = page.getUrls().size() / page.getItemsPerPage();}

    <div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
        @for(int i = 1; i < pageCount; i++)
            <button type="button" class="btn btn-primary"><a href="${NamedRoutes.urlsPath(i)}">i</a></button>
        @endfor
    </div>

    !{var urls = page.getUrls();}
    @if(!urls.isEmpty())
        <table class="table table-light table-hover table-striped table-bordered border-success">
            <thead>
            <tr>
                <th scope="col">ID</th>
                <th scope="col">Название</th>
            </tr>
            </thead>
            <tbody>
            @for(Url item : urls)
                <tr>
                    <th scope="row">${item.getId()}</th>
                    <td>${item.getName()}</td>
                </tr>
            @endfor
            </tbody>
        </table>
    @else
        <div class="alert alert-warning shadow-sm p-3 mb-5 bg-body rounded" role="alert">
            Список пуст
            <button type="button" class="btn-close position-absolute top-50 start-100 translate-middle" aria-label="Close"></button>
        </div>
    @endif

`
)