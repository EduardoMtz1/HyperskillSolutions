<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Latest</title>
    <link rel="stylesheet"
           href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css">
    <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
</head>
<body>
    <#list latestCodes as singleCode>
    <div>
        <span id="load_date">${singleCode.date}</span>
        <pre id="code_snippet">
            <code>
                ${singleCode.code}
            </code>
        </pre>
    </div>
    </#list>
</body>
</html>