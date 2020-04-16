# coding: utf-8

"""
    Polemarch

     ### Polemarch is ansible based service for orchestration infrastructure.  * [Documentation](http://polemarch.readthedocs.io/) * [Issue Tracker](https://gitlab.com/vstconsulting/polemarch/issues) * [Source Code](https://gitlab.com/vstconsulting/polemarch)    # noqa: E501

    OpenAPI spec version: v2
    
    Generated by: https://github.com/swagger-api/swagger-codegen.git
"""


import pprint
import re  # noqa: F401

import six


class WidgetSetting(object):
    """NOTE: This class is auto generated by the swagger code generator program.

    Do not edit the class manually.
    """

    """
    Attributes:
      swagger_types (dict): The key is attribute name
                            and the value is attribute type.
      attribute_map (dict): The key is attribute name
                            and the value is json key in definition.
    """
    swagger_types = {
        'active': 'bool',
        'collapse': 'bool',
        'sort': 'int'
    }

    attribute_map = {
        'active': 'active',
        'collapse': 'collapse',
        'sort': 'sort'
    }

    def __init__(self, active=True, collapse=False, sort=None):  # noqa: E501
        """WidgetSetting - a model defined in Swagger"""  # noqa: E501

        self._active = None
        self._collapse = None
        self._sort = None
        self.discriminator = None

        if active is not None:
            self.active = active
        if collapse is not None:
            self.collapse = collapse
        if sort is not None:
            self.sort = sort

    @property
    def active(self):
        """Gets the active of this WidgetSetting.  # noqa: E501


        :return: The active of this WidgetSetting.  # noqa: E501
        :rtype: bool
        """
        return self._active

    @active.setter
    def active(self, active):
        """Sets the active of this WidgetSetting.


        :param active: The active of this WidgetSetting.  # noqa: E501
        :type: bool
        """

        self._active = active

    @property
    def collapse(self):
        """Gets the collapse of this WidgetSetting.  # noqa: E501


        :return: The collapse of this WidgetSetting.  # noqa: E501
        :rtype: bool
        """
        return self._collapse

    @collapse.setter
    def collapse(self, collapse):
        """Sets the collapse of this WidgetSetting.


        :param collapse: The collapse of this WidgetSetting.  # noqa: E501
        :type: bool
        """

        self._collapse = collapse

    @property
    def sort(self):
        """Gets the sort of this WidgetSetting.  # noqa: E501


        :return: The sort of this WidgetSetting.  # noqa: E501
        :rtype: int
        """
        return self._sort

    @sort.setter
    def sort(self, sort):
        """Sets the sort of this WidgetSetting.


        :param sort: The sort of this WidgetSetting.  # noqa: E501
        :type: int
        """

        self._sort = sort

    def to_dict(self):
        """Returns the model properties as a dict"""
        result = {}

        for attr, _ in six.iteritems(self.swagger_types):
            value = getattr(self, attr)
            if isinstance(value, list):
                result[attr] = list(map(
                    lambda x: x.to_dict() if hasattr(x, "to_dict") else x,
                    value
                ))
            elif hasattr(value, "to_dict"):
                result[attr] = value.to_dict()
            elif isinstance(value, dict):
                result[attr] = dict(map(
                    lambda item: (item[0], item[1].to_dict())
                    if hasattr(item[1], "to_dict") else item,
                    value.items()
                ))
            else:
                result[attr] = value
        if issubclass(WidgetSetting, dict):
            for key, value in self.items():
                result[key] = value

        return result

    def to_str(self):
        """Returns the string representation of the model"""
        return pprint.pformat(self.to_dict())

    def __repr__(self):
        """For `print` and `pprint`"""
        return self.to_str()

    def __eq__(self, other):
        """Returns true if both objects are equal"""
        if not isinstance(other, WidgetSetting):
            return False

        return self.__dict__ == other.__dict__

    def __ne__(self, other):
        """Returns true if both objects are not equal"""
        return not self == other