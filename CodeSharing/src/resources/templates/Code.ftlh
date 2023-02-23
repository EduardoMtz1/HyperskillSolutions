<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Code</title>
    <link rel="stylesheet"
           href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css">
    <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
</head>
<body>
    <span id = "load_date">${code.date}</span>
    <#if code.timeLimit && (code.time >= 0)>
        <p>
            The code will be available for <span id="time_restriction">${code.time}</span> seconds
        </p>
    </#if>
    <#if code.viewLimit && (code.views >= 0)>
            <p>
                <span id="views_restriction">${code.views}</span> more views allowed
            </p>
        </#if>
    <pre id = "code_snippet">
        <code>
            ${code.code}
        </code>
    </pre>
</body>
</html>