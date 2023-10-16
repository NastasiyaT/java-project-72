@import hexlet.code.dto.urls.UrlPage
@param UrlPage page

@template.layout.page(
    content = @`
        <div class="mx-4 mt-4 mb-0">
            <div class="flex-row p-1">
                <h3>Сайт <span class="fw-light fst-italic">${page.getUrl().getName()}</span></h3>
            </div>
            <div class="flex-row fs-6">
                <table class="table table-light table-hover table-bordered border-white border-4">
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
                        <td>${page.getUrl().getCreatedAt().toString()}</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    `
)
