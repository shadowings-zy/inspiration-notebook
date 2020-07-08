import katex from "katex"

/**
 * 笔记的diff算法，用于比较两个contentList的不同。
 * 针对某个笔记来说，对于其修改其实都是同一种情况：
 * 先减少n行后再增加m行（减少、新增的行都是连续的）。
 *
 * 比如：
 * 按下回车，就是减少原来的1行后新增加2行。
 * 按下backspace删除一行，就是减少2行后增加1行。
 * 修改某一行，就是减少1行后再增加1行。
 * 选中m行并粘贴替换n行，就是减少m行后再增加n行。
 *
 * 所以我们需要先遍历oldList，找到哪一段连续的行与newList不一致，删去它们。
 * 然后再将newList中新的不一致的行添加进来。
 */
function diff(oldList, newList) {
  return getDifferentIndex(oldList, newList)
}

/**
 * 获取两者不一样的开始位置和结束位置
 * 因为不一致的位置是连续的，
 * 所以寻找开始位置时，我们从前向后寻找第一个不同位置，
 * 寻找结束位置时，我们从后向前寻找第一个不同位置，中间即为不同
 */
function getDifferentIndex(oldList, newList) {
  let a = 0
  let b = 1
  let startIndex = 0
  let oldEndIndex = oldList.length - b
  let newEndIndex = newList.length - b

  // 找到两者不一样的开始点
  while (a < oldList.length || a < newList.length) {
    if (oldList[a] !== newList[a]) {
      startIndex = a
      break
    }
    a++
  }

  // 找到两者不一样的结束点
  while (b <= oldList.length || b <= newList.length) {
    if (oldList[oldList.length - b] !== newList[newList.length - b]) {
      oldEndIndex = oldList.length - b
      newEndIndex = newList.length - b
      break
    }
    // 如果b从后向前寻找回startIndex时，也结束循环
    if (b + startIndex >= oldList.length || b + startIndex >= newList.length) {
      oldEndIndex = oldList.length - b
      newEndIndex = newList.length - b
      break
    }
    b++
  }

  return {
    startIndex,
    oldEndIndex,
    newEndIndex
  }
}

/**
 * 翻译markdown字符串中的公式
 * @param str
 * @returns {void|string|*}
 */
function parse(str) {
  return parse$(parse$$(str))
}

/**
 * 编译markdown字符串中的多行公式
 * @param str
 * @returns {void | string | *}
 */
function parse$$(str) {
  const strArray = str.split("")
  let dollarFlag = false // 是否匹配到了$
  let mathStr = ""
  for (let a = 0; a < strArray.length - 1; a++) {
    let testStr = strArray[a] + strArray[a + 1]
    if (testStr === "$$" && !dollarFlag) {
      dollarFlag = true
    } else if (testStr === "$$" && dollarFlag) {
      dollarFlag = false
      mathStr = mathStr.substr(1, mathStr.length)
      let parseMathStr = parseMath(mathStr)
      str = str.replace("$$" + mathStr + "$$", "<p>" + parseMathStr + "</p>")
      mathStr = ""
    } else if (testStr !== "$$" && dollarFlag) {
      mathStr += strArray[a]
    }
  }

  return str
}

/**
 * 编译markdown中的单行公式
 * @param str
 * @returns {void | string | *}
 */
function parse$(str) {
  const strArray = str.split("")
  let dollarFlag = false // 是否匹配到了$
  let mathStr = ""
  for (let a = 0; a < strArray.length; a++) {
    let testStr = strArray[a]
    if (testStr === "$" && !dollarFlag) {
      dollarFlag = true
    } else if (testStr === "$" && dollarFlag) {
      dollarFlag = false
      let parseMathStr = parseMath(mathStr)
      str = str.replace("$" + mathStr + "$", parseMathStr)
      mathStr = ""
    } else if (testStr !== "$" && dollarFlag) {
      mathStr += strArray[a]
    }
  }

  return str
}

/**
 * 将公式字符串转换为HTML
 * @param mathStr
 * @returns {*}
 */
function parseMath(mathStr) {
  return katex.renderToString(mathStr, {
    throwOnError: false,
    output: "html",
    strict: "ignore ",
    displayMode: mathStr.includes("\\tag{")
  })
}

/**
 * 同步两个滚动条的长度
 * @param fromElement
 * @param toElement
 */
function setScroll(fromElement, toElement) {
  let temp = fromElement.scrollTop / fromElement.scrollHeight
  toElement.scrollTop = Math.floor(temp * toElement.scrollHeight)
}

// /**
//  * 获取滚动条的值
//  * @returns {number}
//  */
// function getScrollTop(element) {
//   let scroll_top = 0;
//   if (element && element.scrollTop) {
//     scroll_top = element.scrollTop;
//   }
//   else if (document.body) {
//     scroll_top = document.body.scrollTop;
//   }
//   return scroll_top;
// }

export {
  diff,
  parse,
  setScroll
}