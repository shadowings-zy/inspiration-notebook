import extend from 'extend'
import {
  getEvent,
  getEventListenerMethod,
  getBoundingClientRect,
  getDomPath,
  querySelector
} from './utils'

const defaultOptions = {
  useClass: false,
  events: ['mousedown', 'keydown'],
}

class Tracker {
  constructor() {
    this.trackerInitialized = false
    this.trackerOptions = defaultOptions
    this.nightWatchCode = "the night watch code is:\n"
  }

  config(options = {}) {
    options = extend(true, {}, defaultOptions, options)
    options.event = defaultOptions.event
    this.trackerOptions = options
  }

  captureEvents() {
    const self = this
    const events = this.trackerOptions.events
    const eventMethodObj = getEventListenerMethod()
    for (let i = 0, j = events.length; i < j; i++) {
      let eventName = events[i]
      document.body[eventMethodObj.addMethod](eventMethodObj.prefix + eventName, function (event) {
        const eventFix = getEvent(event)
        if (!eventFix) {
          return
        }
        const domTrackData = self.handleDomPathTrack(eventFix)
        const positionTrackData = self.handlePositionTrack(eventFix)
        const inputTrackData = self.handleInputTrack(eventFix)
        const trackData = extend(true, domTrackData, positionTrackData, inputTrackData)
        self.generateSpecs(trackData)
      }, false)
    }
  }

  handleDomPathTrack(event) {
    const domPath = getDomPath(event.target, this.trackerOptions.useClass)
    return {
      domPath: domPath,
      trackingType: event.type
    }
  }

  handlePositionTrack(event) {
    const rect = getBoundingClientRect(event.target)
    if (rect.width === 0 || rect.height === 0) {
      return
    }
    if (event.pageX !== undefined || event.clientX !== undefined) {
      let t = document.documentElement || document.body.parentNode
      const scrollX = (t && typeof t.scrollLeft == 'number' ? t : document.body).scrollLeft
      const scrollY = (t && typeof t.scrollTop == 'number' ? t : document.body).scrollTop
      const pageX = event.pageX || event.clientX + scrollX
      const pageY = event.pageY || event.clientY + scrollY
      return {
        offsetX: ((pageX - rect.left - scrollX) / rect.width).toFixed(6),
        offsetY: ((pageY - rect.top - scrollY) / rect.height).toFixed(6),
      }
    }
  }

  handleInputTrack(event) {
    if (event.key !== undefined) {
      return {
        inputKey: event.key
      }
    }
  }

  generateSpecs(trackData) {
    if (trackData.trackingType === 'mousedown') {
      this.nightWatchCode += '.assert.elementPresent("' + trackData.domPath + '")' + '\n'
      this.nightWatchCode += '.click("' + trackData.domPath + '").pause(clickTime)' + '\n'
    } else if (trackData.trackingType === 'keydown') {
      this.nightWatchCode += '.keys("' + trackData.inputKey + '").pause(inputTime)' + '\n'
    }
  }

  showSpecs() {
    return this.nightWatchCode
  }

  init() {
    if (!this.trackerInitialized) {
      this.captureEvents()
      this.trackerInitialized = true
    }
    return this
  }
}

export default Tracker