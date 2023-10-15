@import hexlet.code.util.NamedRoutes

@template.layout.page(
    content = @`
        <form action="${NamedRoutes.rootPath()}" method="post">
            <h6>Введите адрес ссылки:</h6>
            <div class="form-floating row row-cols-lg-auto g-3 align-items-center">
                <label for="inputUrl" class="form-label">Ссылка</label>
                <input type="url" class="form-control" id="inputUrl" name="name">
                <div class="form-text">Пример: https://www.example.com</div>
            </div>
            <div>
                <button type="submit" class="btn btn-primary">Проверить</button>
            </div>
        </form>
    `
)