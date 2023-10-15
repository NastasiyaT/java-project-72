@import hexlet.code.dto.urls.UrlPage
@param UrlPage page

@template.layout.page(
    content = @`
        <div class="fw-bold">
            <h1>Сайт ${page.getUrl().getName()}</h1>
        </div>
        <table class="table table-light table-hover border border-light border-2">
            <tbody>
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
                <td>${page.getUrl().getCreatedAt().toString()}</td>
            </tr>
            </tbody>
        </table>
    `
)
